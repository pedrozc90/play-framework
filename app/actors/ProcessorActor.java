package actors;

import actors.objects.ChangeLog;
import actors.objects.ChangeLogEntry;
import actors.objects.Resolved;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.objects.RequestDto;
import controllers.objects.RequestItemDto;
import models.*;
import play.db.jpa.JPA;
import play.libs.Json;
import repositories.tuples.PurchaseOrderTuple;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import services.*;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessorActor extends AbstractLoggingActor {

    private final ActorRef dispatcher;

    private final PurchaseOrderService purchaseOrderService = PurchaseOrderService.getInstance();
    private final OrderService orderService = OrderService.getInstance();
    private final OrderItemService orderItemService = OrderItemService.getInstance();
    private final SupplierService supplierService = SupplierService.getInstance();
    private final ProductService productService = ProductService.getInstance();

    public static Props props(final ActorRef dispatcher) {
        return Props.create(ProcessorActor.class, () -> new ProcessorActor(dispatcher));
    }

    public ProcessorActor(final ActorRef dispatcher) {
        this.dispatcher = dispatcher;
        receive(createReceive());
    }

    private PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(PurchaseOrderTuple.class, this::onMessage)
            .matchAny(this::unhandled)
            .build();
    }

    @Override
    public void preStart() throws Exception {
        log().info("{} started", self().path());
    }

    @Override
    public void postStop() throws Exception {
        log().info("{} stopped", self().path());
    }

    private void onMessage(final PurchaseOrderTuple tuple) {
        log().info("PurchaseOrder {} - {} is being processed.", tuple.getId(), tuple.getNumber());

        try {
            JPA.withTransaction(() -> {
                final EntityManager em = JPA.em();

                final PurchaseOrder purchaseOrder = purchaseOrderService.get(tuple.getId());
                log().info("PurchaseOrder loaded -> id: {}, number: {}, version: {}", purchaseOrder.getId(), purchaseOrder.getNumber(), purchaseOrder.getVersion());

                em.lock(purchaseOrder, LockModeType.PESSIMISTIC_WRITE);
                final String content = purchaseOrder.getContent();

                final JsonNode json = Json.parse(content);
                final RequestDto dto = Json.fromJson(json, RequestDto.class);

                final Resolved<Supplier> supplierResolved = supplierService.resolve(dto.getSupplier());
                final Supplier supplier = supplierResolved.getEntity();

                final Resolved<Order> orderResolved = orderService.resolve(dto.getNumber(), dto.getStatus(), supplier);
                final Order order = orderResolved.getEntity();

                final List<ChangeLogEntry> changes = dto.getItems().stream()
                    .map((row) -> {
                        final Resolved<Product> productResolved = productService.resolve(row);
                        final Resolved<OrderItem> itemResolved = orderItemService.resolve(row, productResolved.getEntity(), order);

                        final List<ChangeLogEntry> entries = new ArrayList<>();
                        entries.addAll(productResolved.getChanges());
                        entries.addAll(itemResolved.getChanges());
                        return entries;
                    })
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

                log().info("Order {} saved", order.getId());

                final ChangeLog changelog = new ChangeLog();
                changelog.getChanges().addAll(supplierResolved.getChanges());
                changelog.getChanges().addAll(orderResolved.getChanges());
                changelog.getChanges().addAll(changes);

                purchaseOrder.setStatus(PurchaseOrder.Status.DONE);
                purchaseOrder.setOrder(order);
                purchaseOrder.setChangelog(Json.toJson(changelog).toPrettyString());

                log().info("PurchaseOrder {} update to DONE", purchaseOrder.getId());
            });
        } catch (Exception e) {
            log().error(e, "PurchaseOrder {} process failed.", tuple.getNumber());
        } finally {
            dispatcher.tell(new DispatcherActor.Dequeue(tuple), self());
            getContext().stop(self());
        }
    }

    private Order resolveOrder(final RequestDto dto) {
        final Resolved<Supplier> resolvedSupplier = supplierService.resolve(dto.getSupplier());
        final Supplier supplier = resolvedSupplier.getEntity();

        Order order = orderService.get(dto.getNumber());
        if (order == null) {
            order = new Order();
            order.setNumber(dto.getNumber());
        }

        if (order.getStatus() == null || order.getStatus().equals(dto.getStatus())) {
            order.setStatus(dto.getStatus());
        }

        if (order.getSupplier() != supplier) {
            order.setSupplier(supplier);
        }

        order = orderService.save(order);

        final Set<OrderItem> items = resolveOrderItem(dto.getItems(), order);

        // TODO: you cannot update the collection 'item' of a exists 'order', hibernate is fucking dum...
        // _order.setItems(items);

        return order;
    }

    private OrderItem resolveOrderItem(final RequestItemDto dto, final Order order) {
        final Resolved<Product> resolved = productService.resolve(dto);
        final Product product = resolved.getEntity();

        OrderItem item = orderItemService.get(product, order);
        if (item == null) {
            item = new OrderItem();
        }

        item.setQuantity(dto.getQuantity());
        item.setLabelType(dto.getLabelType());
        item.setProduct(product);
        item.setOrder(order);

        // same as em.merge(item)
        return orderItemService.save(item);
    }

    private Set<OrderItem> resolveOrderItem(final Set<RequestItemDto> dtos, final Order order) {
        return dtos.stream()
            .map((item) -> resolveOrderItem(item, order))
            .collect(Collectors.toSet());
    }

}
