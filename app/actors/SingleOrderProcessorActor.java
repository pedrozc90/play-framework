package actors;

import actors.objects.ChangeLog;
import actors.objects.ChangeLogEntry;
import actors.objects.Resolved;
import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.objects.RequestDto;
import models.*;
import play.db.jpa.JPA;
import play.libs.Json;
import repositories.tuples.PurchaseOrderTuple;
import scala.PartialFunction;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;
import services.*;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SingleOrderProcessorActor extends AbstractLoggingActor {

    public static Props props() {
        return Props.create(SingleOrderProcessorActor.class, SingleOrderProcessorActor::new);
    }

    private final Queue<PurchaseOrderTuple> queue = new LinkedList<>();
    private boolean busy = false;

    private final PurchaseOrderService purchaseOrderService = PurchaseOrderService.getInstance();
    private final OrderService orderService = OrderService.getInstance();
    private final OrderItemService orderItemService = OrderItemService.getInstance();
    private final SupplierService supplierService = SupplierService.getInstance();
    private final ProductService productService = ProductService.getInstance();

    // === Messages ===
    public static class Tick {
    }

    public static class ProcessNext {
    }

    public SingleOrderProcessorActor() {
        receive(createReceive());
    }

    private PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(Tick.class, this::onTick)
            .match(ProcessNext.class, this::onProcessNext)
            .matchAny(this::unhandled)
            .build();
    }

    @Override
    public void preStart() {
        log().info("SingleOrderProcessorActor started.");
        scheduleTick();
    }

    @Override
    public void postStop() {
        log().info("SingleOrderProcessorActor stopped.");
    }

    private void scheduleTick() {
        getContext().system().scheduler().scheduleOnce(
            Duration.create(30, TimeUnit.SECONDS),
            self(),
            new Tick(),
            getContext().dispatcher(),
            self()
        );
    }

    private void onTick(final Tick obj) {
        log().info("Running scheduled check for WAITING orders.");
        try {
            final List<PurchaseOrderTuple> toEnqueue = new ArrayList<>();

            // Load WAITING orders and set to ONGOING in a transaction
            JPA.withTransaction(() -> {
                final List<PurchaseOrder> waiting = purchaseOrderService.findWaitingOrders(); // Ordered by ID ASC
                for (PurchaseOrder po : waiting) {
                    po.setStatus(PurchaseOrder.Status.ONGOING);
                    toEnqueue.add(new PurchaseOrderTuple(po));
                    log().info("Marked PurchaseOrder {} as ONGOING", po.getId());
                }
            });

            for (PurchaseOrderTuple tuple : toEnqueue) {
                queue.offer(tuple);
                log().info("Queued PurchaseOrder {}", tuple.getId());
            }

            // If not busy, begin processing
            if (!busy) processNext();

        } catch (Exception e) {
            log().error(e, "Failed to fetch and queue WAITING orders.");
        } finally {
            scheduleTick(); // Reschedule regardless of success
        }
    }

    private void onProcessNext(final ProcessNext obj) {
        processNext();
    }

    private void processNext() {
        if (queue.isEmpty()) {
            busy = false;
            return;
        }

        busy = true;
        final PurchaseOrderTuple tuple = queue.poll();

        // global dispatcher shared across all actors
        // use when:
        // - use for blocking or long-running work
        // - want to offload work outside of the actor’s own dispatcher
        // - more threads
        final ExecutionContextExecutor dispatcher = getContext().system().dispatcher();

        // it own actor dispatcher
        // use when:
        // - for short/non-blocking work
        // - stay consistent with the actor’s own thread policy
        // final ExecutionContextExecutor dispatcher = getContext().dispatcher();

        dispatcher.execute(() -> processPurchaseOrder(tuple.getId(), tuple.getNumber()));
    }

    private void processPurchaseOrder(final Long id, final String number) {
        try {
            JPA.withTransaction(() -> {
                final PurchaseOrder po = purchaseOrderService.get(id);
                JPA.em().lock(po, LockModeType.PESSIMISTIC_WRITE);
                po.setStatus(PurchaseOrder.Status.ONGOING);
                log().info("Processing PurchaseOrder {} - {}", po.getId(), po.getNumber());
            });

            JPA.withTransaction(() -> {
                final EntityManager em = JPA.em();

                final PurchaseOrder purchaseOrder = purchaseOrderService.get(id);
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

            JPA.withTransaction(() -> {
                final PurchaseOrder po = purchaseOrderService.get(id);
                JPA.em().lock(po, LockModeType.PESSIMISTIC_WRITE);
                po.setStatus(PurchaseOrder.Status.DONE);
                log().info("Marked PurchaseOrder {} as DONE", po.getId());
            });
        } catch (Exception e) {
            log().error(e, "Failed to process PurchaseOrder {}", id);
            // Optional: maybe revert status to WAITING?
        } finally {
            self().tell(new ProcessNext(), self());
        }
    }

}
