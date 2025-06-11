package actors;

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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessorActor extends AbstractLoggingActor {

    private final ActorRef dispatcher;
    private final PurchaseOrderService purchaseOrderService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final SupplierService supplierService;
    private final ProductService productService;

    public static Props props(final ActorRef dispatcher) {
        return Props.create(ProcessorActor.class, () -> new ProcessorActor(dispatcher));
    }

    public ProcessorActor(final ActorRef dispatcher) {
        this.dispatcher = dispatcher;
        this.purchaseOrderService = PurchaseOrderService.getInstance();
        this.orderService = OrderService.getInstance();
        this.orderItemService = OrderItemService.getInstance();
        this.supplierService = SupplierService.getInstance();
        this.productService = ProductService.getInstance();
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

                final Order order = resolveOrder(dto);

                log().info("Order {} saved", order.getId());

                purchaseOrder.setStatus(PurchaseOrder.Status.DONE);
                purchaseOrder.setOrder(order);
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
        final Supplier supplier = resolveSupplier(dto.getSupplier());

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

    private Supplier resolveSupplier(final String code) {
        Supplier supplier = supplierService.get(code);
        if (supplier == null) {
            supplier = supplierService.create(code);
        }
        return supplier;
    }

    private Product resolveProduct(final RequestItemDto dto) {
        Product product = productService.get(dto.getEan());
        if (product == null) {
            product = new Product();
        }

        final Optional<Map<String, Object>> optMetadata = Optional.ofNullable(dto.getMetadata());
        final String size = optMetadata
            .map(v -> v.get("size"))
            .map(Object::toString)
            .orElse(null);
        final String color = optMetadata
            .map(v -> v.get("color"))
            .map(Object::toString)
            .orElse(null);

        product.setEan(dto.getEan());
        product.setDescription(dto.getDescription());
        product.setSize(size);
        product.setColor(color);

        // same as em.merge(product)
        return productService.save(product);
    }

    private OrderItem resolveOrderItem(final RequestItemDto dto, final Order order) {
        final Product product = resolveProduct(dto);

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
