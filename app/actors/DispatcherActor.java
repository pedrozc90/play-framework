package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import repositories.tuples.PurchaseOrderTuple;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.*;

public class DispatcherActor extends AbstractLoggingActor {

    private final int num; // maximum number of workers
    private final Queue<PurchaseOrderTuple> queue = new LinkedList<>();
    private final Map<String, ActorRef> workers = new HashMap<>();

    public DispatcherActor(final int num) {
        this.num = num;
        receive(createReceive());
    }

    private PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(Enqueue.class, this::onEnqueue)
            .match(Dequeue.class, this::onDequeue)
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

    private void onEnqueue(final Enqueue obj) {
        if (obj == null || obj.tuple == null) {
            log().error("Received null enqueue message");
            return;
        }

        queue.offer(obj.tuple);
        log().info("PurchaseOrder {} added to the queue", obj.tuple.getId());

        dispatch();
    }

    private void dispatch() {
        if (workers.size() >= num) {
            log().info("Queue is full, skipping dispatch");
            return;
        }

        final Iterator<PurchaseOrderTuple> it = queue.iterator();
        while (it.hasNext()) {
            final PurchaseOrderTuple tuple = it.next();
            final String number = tuple.getNumber();

            if (workers.containsKey(number)) {
                log().info("Worker already exists for {}", number);
                continue;
            }

            it.remove();

            log().info("Creating worker for PurchaseOrder {} - {}", tuple.getId(), number);

            final ActorRef workerRef = workers.computeIfAbsent(number, v -> {
                return getContext().actorOf(ProcessorActor.props(self()));
            });

            workers.put(number, workerRef);

            workerRef.tell(tuple, self());

            if (workers.size() >= num) break;
        }
    }

    private void onDequeue(final Dequeue obj) {
        final PurchaseOrderTuple tuple = obj.tuple;
        final String number = tuple.getNumber();
        log().info("Dequeued: {} - {}", tuple.getId(), number);

        // free a worker spot
        final ActorRef workerRef = workers.remove(number);

        // terminate the worker actor
        if (workerRef != null) getContext().stop(workerRef);

        dispatch();
    }

    /**
     * Api
     **/
    public static Props props(final int num) {
        return Props.create(DispatcherActor.class, () -> new DispatcherActor(num));
    }

    /**
     * Messages
     **/
    public static class Enqueue {

        public final PurchaseOrderTuple tuple;

        public Enqueue(final PurchaseOrderTuple tuple) {
            this.tuple = tuple;
        }

    }

    public static class Dequeue {

        public final PurchaseOrderTuple tuple;

        public Dequeue(final PurchaseOrderTuple tuple) {
            this.tuple = tuple;
        }

    }

}
