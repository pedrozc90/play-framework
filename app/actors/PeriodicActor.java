package actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import play.db.jpa.JPA;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import java.util.concurrent.TimeUnit;

public class PeriodicActor extends AbstractLoggingActor {

    private Cancellable scheduler;

    public PeriodicActor() {
        receive(createReceive());
    }

    @Override
    public void preStart() throws Exception {
        self().tell(new Tick(), self());
    }

    @Override
    public void postStop() throws Exception {
        stopScheduler();
    }

    private PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(Tick.class, (o) -> onTick(o))
            .match(Schedule.class, (o) -> onSchedule(o))
            .match(SupervisorActor.Heartbeat.class, (o) -> onHeartbeat(o))
            .matchAny((o) -> log().error("unknown message: {}", o))
            .build();
    }

    private void createScheduler() {
        final FiniteDuration delay = Duration.create(10, TimeUnit.SECONDS);
        scheduler = context().system().scheduler().scheduleOnce(
            delay,
            self(),
            new Tick(),
            context().dispatcher(),
            self()
        );
    }

    private void stopScheduler() {
        if (scheduler != null && !scheduler.isCancelled()) {
            scheduler.cancel();
        }
    }

    private void scheduleNextRun() {
        stopScheduler();
        createScheduler();
    }

    private void onTick(final Tick obj) {
        context().system().dispatcher().execute(() -> {
            try {
                procedure();
            } finally {
                self().tell(new Schedule(), self());
            }
        });
    }

    private void procedure() {
        JPA.withTransaction(() -> {
            log().info("PeriodicActor ticked");
            Thread.sleep(10_000);
        });
    }

    private void onSchedule(final Schedule obj) {
        log().debug("Scheduling next run");
        scheduleNextRun();
    }

    private void onHeartbeat(final SupervisorActor.Heartbeat obj) {
        log().info("PeriodicActor heartbeat received");
        final ActorRef parent = context().parent();
        parent.tell(new SupervisorActor.HeartbeatAck(self()), self());
    }

    /**
     * Api
     **/
    public static Props props() {
        return Props.create(PeriodicActor.class, PeriodicActor::new);
    }

    /**
     * Messages
     **/
    public static class Tick {
    }

    public static class Schedule {
    }

}
