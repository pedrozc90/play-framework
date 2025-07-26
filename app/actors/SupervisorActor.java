package actors;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SupervisorActor extends AbstractLoggingActor {

    private final long HEARTBEAT_THRESHOLD = 2 * 60 * 1000; // 2 minutes
    private final Set<ActorRef> workers = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Map<ActorRef, Long> heartbeats = new ConcurrentHashMap<>();

    private Cancellable heartbeatTask;

    public SupervisorActor() {
        receive(createReceive());
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy();
    }

    @Override
    public void preStart() throws Exception {
        self().tell(new Init(), self());
    }

    @Override
    public void postStop() throws Exception {
        if (heartbeatTask != null && !heartbeatTask.isCancelled()) {
            log().debug("Cancelling heartbeat task");
            heartbeatTask.cancel();
        }
    }

    private OneForOneStrategy strategy() {
        final FiniteDuration interval = Duration.create(1, TimeUnit.MINUTES);
        final PartialFunction<Throwable, SupervisorStrategy.Directive> decider = DeciderBuilder
            .match(Exception.class, (e) -> SupervisorStrategy.restart())
            .build();
        return new OneForOneStrategy(5, interval, decider);
    }

    private PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(Init.class, (o) -> onInit(o))
            .match(Heartbeat.class, (o) -> onHeartbeat(o))
            .match(HeartbeatAck.class, (o) -> onHeartbeatAck(o))
            .match(Terminated.class, (o) -> onTerminated(o))
            .matchAny((o) -> log().error("unknown message: {}", o))
            .build();
    }

    private Cancellable createHeartbeatTask() {
        final FiniteDuration delay = Duration.create(60, TimeUnit.SECONDS);
        final FiniteDuration interval = Duration.create(60, TimeUnit.SECONDS);
        return context().system().scheduler().schedule(
            delay,
            interval,
            self(),
            new Heartbeat(),
            context().dispatcher(),
            self()
        );
    }

    private void scheduleHeartbeatTask() {
        log().debug("Scheduling heartbeat task");
        heartbeatTask = createHeartbeatTask();
    }

    private void onInit(final Init obj) {
        initActor();
        scheduleHeartbeatTask();
    }

    private void onHeartbeat(final Heartbeat obj) {
        final long now = System.currentTimeMillis();
        workers.forEach((ref) -> {
            final long prev = heartbeats.getOrDefault(ref, 0L);
            final long timeout = now - prev;
            if (timeout > HEARTBEAT_THRESHOLD) {
                log().info("Restarting actor {} due to missing heartbeat", ref);

                // stop actor
                stopActor(ref);

                // start a new actor
                initActor();
            } else {
                // send heartbeat to healthy worker
                ref.tell(new Heartbeat(), self());
            }
        });
    }

    private void onHeartbeatAck(final HeartbeatAck obj) {
        final ActorRef ref = obj.actor;
        if (ref != null && workers.contains(ref)) {
            heartbeats.put(ref, System.currentTimeMillis());
        } else {
            log().warning("Received heartbeat ack from unknown actor {}", ref);
            heartbeats.remove(ref);
        }
    }

    private void onTerminated(final Terminated obj) {
        final ActorRef ref = obj.actor();
        if (ref != null) {
            log().warning("Actor {} died unexpectedly, restarting ...", ref);
            workers.remove(ref);
            initActor();
        }
    }

    private ActorRef initActor() {
        final ActorRef ref = context().actorOf(PeriodicActor.props());
        context().watch(ref);
        workers.add(ref);
        heartbeats.put(ref, System.currentTimeMillis());
        return ref;
    }

    private void stopActor(final ActorRef ref) {
        context().stop(ref);
        context().unwatch(ref);
        workers.remove(ref);
        heartbeats.remove(ref);
    }

    /**
     * API
     **/
    public static Props props() {
        return Props.create(SupervisorActor.class, SupervisorActor::new);
    }

    /**
     * Messages
     **/
    public static class Init {
    }

    public static class Heartbeat {
    }

    @Data
    @AllArgsConstructor
    public static class HeartbeatAck {

        private ActorRef actor;

    }

}
