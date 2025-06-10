package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import models.PurchaseOrder;
import play.db.jpa.JPA;
import repositories.PurchaseOrderRepository;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SchedulerActor extends AbstractLoggingActor {

    private final PurchaseOrderRepository repository;

    public static Props props(final PurchaseOrderRepository repository) {
        return Props.create(SchedulerActor.class, () -> new SchedulerActor(repository));
    }

    /**
     * Message definitions to avoid magic strings
     **/
    public static final class CheckOrders {
        public static final CheckOrders INSTANCE = new CheckOrders();

        private CheckOrders() {
        }
    }

    public SchedulerActor(final PurchaseOrderRepository repository) {
        this.repository = repository;
        receive(createReceive());
    }

    @Override
    public void preStart() throws Exception {
        log().info("SchedulerActor started");
        scheduleNextRun();
    }

    @Override
    public void postStop() throws Exception {
        log().info("SchedulerActor stopped");
    }

    public PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(CheckOrders.class, this::handledMessage)
            .matchAny(this::unhandled)
            .build();
    }

    private void handledMessage(final Object obj) {
        if (obj == CheckOrders.INSTANCE) {
            processWaitingOrders();
            scheduleNextRun();
        }
    }

    private void processWaitingOrders() {
        try {
            JPA.withTransaction(() -> {
                List<PurchaseOrder> waitingOrders = repository.findWaitingOrders();
                log().info("Found {} orders in WAITING status", waitingOrders.size());

                for (PurchaseOrder order : waitingOrders) {
                    handleOrder(order);
                }
            });
        } catch (Exception e) {
            log().error(e, "Error in scheduler execution");
        }
    }

    private void handleOrder(PurchaseOrder order) {
        try {
            // Process each order
            // You can send each order to another actor for processing
            log().info("Processing order: {}", order.getNumber());
            // Example: getContext().actorSelection("/user/orderProcessor").tell(order, getSelf());
        } catch (Exception e) {
            log().error("Error processing order " + order.getNumber(), e);
        }
    }

    private void scheduleNextRun() {
        // Schedule next check in 30 seconds
        final FiniteDuration duration = Duration.create(30, TimeUnit.SECONDS);
        getContext().system().scheduler().scheduleOnce(
            duration,
            self(),
            CheckOrders.INSTANCE,
            getContext().dispatcher(),
            self()
        );
    }

}
