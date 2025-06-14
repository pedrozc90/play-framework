package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import models.PurchaseOrder;
import play.db.jpa.JPA;
import repositories.tuples.PurchaseOrderTuple;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;
import services.PurchaseOrderService;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

// Core idea:
// - Scheduler fetches WAITING records, marks as ONGOING
// - Dispatcher maintains a global queue and launches limited workers
// - Each worker handles one order at a time, per number ordering is handled centrally
public class SchedulerActor extends AbstractLoggingActor {

    private final ActorRef dispatcher;

    private final PurchaseOrderService purchaseOrderService = PurchaseOrderService.getInstance();

    public static Props props() {
        return Props.create(SchedulerActor.class, SchedulerActor::new);
    }

    /**
     * Message definitions to avoid magic strings
     **/
    public static final class CheckOrders {
    }

    public static final class ResumeOngoingOrders {
    }

    public SchedulerActor() {
        this.dispatcher = getContext().actorOf(DispatcherActor.props(5), "dispatcher");
        receive(createReceive());
    }

    public PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(CheckOrders.class, this::onCheckOrders)
            .match(ResumeOngoingOrders.class, this::onResumeOngoingOrders)
            .matchAny(this::unhandled)
            .build();
    }

    @Override
    public void preStart() throws Exception {
        log().info("{} started", self().path());
        scheduleNextRun();
    }

    @Override
    public void postStop() throws Exception {
        log().info("{} stopped", self().path());
    }

    private void onCheckOrders(final CheckOrders obj) {
        processWaitingOrders();
        scheduleNextRun();
    }

    private void processWaitingOrders() {
        try {
            final Set<PurchaseOrderTuple> toDispatch = new LinkedHashSet<>();

            JPA.withTransaction(() -> {
                // filter only purchase orders where status = WAITING and order by id ASC
                // set lock mode to PESSIMISTIC_WRITE
                final List<PurchaseOrder> list = purchaseOrderService.findWaitingOrders();
                for (PurchaseOrder row : list) {
                    // updates entity status to ONGOING
                    final PurchaseOrderTuple tuple = handlePurchaseOrder(row);
                    if (tuple != null) {
                        // only store IDs/references, not full entity
                        toDispatch.add(tuple);
                    }
                }
            });

            // now that the ONGOING updates are committed, safely dispatch
            for (PurchaseOrderTuple tuple : toDispatch) {
                dispatchToWorker(tuple);
            }
        } catch (Exception e) {
            log().error(e, "Error in scheduler execution");
        }
    }

    private void onResumeOngoingOrders(final ResumeOngoingOrders obj) {
        // Optional: revert ONGOING back to WAITING for all orders that got stuck
        try {
            JPA.withTransaction(() -> {
                final List<PurchaseOrder> list = purchaseOrderService.findOngoing();
                for (PurchaseOrder row : list) {
                    row.setStatus(PurchaseOrder.Status.WAITING);
                }
                log().info("Reverted {} ONGOING orders back to WAITING", list.size());
            });
        } catch (Exception e) {
            log().error(e, "Failed to revert ONGOING orders to WAITING");
        }

        // After reverting, immediately kick off a normal check to pick up those WAITING orders
        self().tell(new CheckOrders(), self());
    }

    private PurchaseOrderTuple handlePurchaseOrder(final PurchaseOrder purchaseOrder) {
        if (purchaseOrder != null && purchaseOrder.getStatus() == PurchaseOrder.Status.WAITING) {
            purchaseOrder.setStatus(PurchaseOrder.Status.ONGOING);
            log().info("PurchaseOrder {} updated to ONGOING", purchaseOrder.getId());
            return new PurchaseOrderTuple(purchaseOrder);
        }
        return null;
    }

    private void dispatchToWorker(final PurchaseOrderTuple tuple) {
        dispatcher.tell(new DispatcherActor.Enqueue(tuple), self());
        log().info("PurchaseOrder {} dispatched", tuple.getId());
    }

    private void scheduleNextRun() {
        // Schedule next check in 30 seconds
        final FiniteDuration duration = Duration.create(30, TimeUnit.SECONDS);
        getContext().system().scheduler().scheduleOnce(
            duration,
            self(),
            new CheckOrders(),
            getContext().dispatcher(),
            self()
        );
    }

}
