package actors.base;

import actors.messages.Schedule;
import actors.messages.Tick;
import akka.actor.Cancellable;
import akka.japi.pf.UnitPFBuilder;
import scala.concurrent.duration.FiniteDuration;

public abstract class BasePeriodicActor extends BaseActor {

    private Cancellable scheduler;

    @Override
    protected UnitPFBuilder<Object> defaultReceive() {
        return super.defaultReceive()
            .match(Schedule.class, this::onSchedule);
    }

    protected void onSchedule(final Schedule obj) {
        final FiniteDuration delay = createDelay();
        scheduler = createScheduler(delay);
    }

    protected abstract FiniteDuration createDelay();

    protected Cancellable createScheduler(final FiniteDuration delay) {
        if (delay.toMillis() < 0) {
            throw new UnsupportedOperationException("Delay can not be negative");
        }

        log().debug("Scheduling periodic task with delay {}", delay);

        return context().system().scheduler().scheduleOnce(
            delay,
            self(),
            new Tick(),
            context().dispatcher(),
            self()
        );
    }

    protected void stopScheduler() {
        if (scheduler != null && !scheduler.isCancelled()) {
            log().debug("Cancelling periodic task");
            scheduler.cancel();
        }
    }

}
