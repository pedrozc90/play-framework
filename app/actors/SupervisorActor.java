package actors;

import actors.files.FileDispatcherActor;
import actors.messages.Heartbeat;
import actors.messages.HeartbeatAck;
import actors.messages.Init;
import actors.shared.BaseActor;
import actors.tasks.TaskExecutor;
import akka.actor.*;
import akka.japi.Creator;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import application.files.FileStorageService;
import application.jobs.JobService;
import application.tasks.TaskService;
import lombok.Data;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.runtime.BoxedUnit;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SupervisorActor extends BaseActor {

    private final long HEARTBEAT_THRESHOLD = 2 * 60 * 1_000; // heartbeat timeout, if a actor do not response in time we reset it
    private final Map<Class<?>, ActorRef> children = new ConcurrentHashMap<>();
    private final Map<Class<?>, Creator<ActorRef>> factories = new ConcurrentHashMap<>();
    private final Map<ActorRef, Class<?>> classes = new ConcurrentHashMap<>();
    private final Map<ActorRef, Long> heartbeats = new ConcurrentHashMap<>();

    private final JobService jobService;
    private final TaskService taskService;
    private final FileStorageService fsService;
    private final TaskExecutor executor;

    private Cancellable heartbeat;

    // Guice injects these via bindActor()
    @Inject
    public SupervisorActor(
        final JobService jobService,
        final TaskService taskService,
        final FileStorageService fsService,
        final TaskExecutor executor
    ) {
        super();
        this.jobService = jobService;
        this.taskService = taskService;
        this.fsService = fsService;
        this.executor = executor;
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy();
    }

    @Override
    public void preStart() {
        super.preStart();

    }

    @Override
    public void postStop() {
        super.postStop();
        stopHeartbeatScheduler();
    }

    // SUPERVISOR STRATEGY
    private OneForOneStrategy strategy() {
        final int retries = 10;
        final FiniteDuration duration = Duration.create(30, TimeUnit.SECONDS);
        final PartialFunction<Throwable, SupervisorStrategy.Directive> decider = DeciderBuilder
            .match(Exception.class, (e) -> SupervisorStrategy.restart())
            .build();
        return new OneForOneStrategy(retries, duration, true, decider);
    }

    // SCHEDULER
    private Cancellable createHeartbeatScheduler() {
        final FiniteDuration delay = Duration.create(5, TimeUnit.SECONDS);
        final FiniteDuration interval = Duration.create(60, TimeUnit.SECONDS);
        return getContext().system().scheduler().schedule(
            delay,
            interval,
            self(),
            new Heartbeat(),
            getContext().dispatcher(),
            self()
        );
    }

    private void startHeartbeatScheduler() {
        logger.info("Starting heartbeat scheduler");
        heartbeat = createHeartbeatScheduler();
    }

    private void stopHeartbeatScheduler() {
        if (heartbeat != null && !heartbeat.isCancelled()) {
            logger.info("Stopping heartbeat scheduler");
            heartbeat.cancel();
        }
    }

    // MESSAGE HANDLERS
    @Override
    protected PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(Init.class, this::onInit)
            .match(Forward.class, this::onForward)
            .match(Heartbeat.class, this::onHeartbeat)
            .match(HeartbeatAck.class, this::onHeartbeatAck)
            .match(Terminated.class, this::onTerminated)
            .matchAny(this::unhandled)
            .build();
    }

    @Override
    protected void onInit(final Init obj) {
        // initialize child actors
        registerActor(FileDispatcherActor.class, FileDispatcherActor.props(jobService, taskService, fsService, executor, 10), "FileDispatcherActor");
        // registerActor(FileProcessorActor.class, FileProcessorActor.props(self()), "FileProcessorActor");

        // create and start heartbeat scheduler
        startHeartbeatScheduler();
    }

    private void onForward(final Forward cmd) {
        final ActorRef ref = children.get(cmd.clazz);
        if (ref != null) {
            ref.forward(cmd.message, context());
        } else {
            logger.error("No registered actor for {}", cmd.clazz);
        }
    }

    @Override
    protected void onHeartbeat(final Heartbeat obj) {
        super.onHeartbeat(obj);

        final long start = System.currentTimeMillis();

        children.forEach((clazz, ref) -> {
            final Long last = heartbeats.get(ref);
            if (last == null || (last - start >= HEARTBEAT_THRESHOLD)) {
                logger.info("Restarting actor {} due to missing heartbeat.", ref);
                stopActor(ref);
                registerActor(clazz, factories.get(clazz));
            } else {
                // send heartbeat to healthy worker
                ref.tell(new Heartbeat(), self());
            }
        });
    }

    @Override
    protected void onHeartbeatAck(final HeartbeatAck obj) {
        super.onHeartbeatAck(obj);
        final ActorRef ref = obj.getActor();
        if (ref != null && children.containsValue(ref)) {
            heartbeats.put(ref, System.currentTimeMillis());
        } else {
            logger.warn("Received heartbeat ack from unknown actor {}", ref);
        }
    }

    @Override
    protected void onTerminated(final Terminated obj) {
        final ActorRef ref = obj.actor();
        logger.info("Actor {} died unexpectedly, restarting it...", ref);

        final Class<?> clazz = classes.get(ref);
        if (clazz != null) {
            final Creator<ActorRef> creator = factories.get(clazz);
            registerActor(clazz, creator);
        }
    }

    // ACTORS
    private ActorRef registerActor(final Class<?> clazz, final Props props, final String name) {
        return registerActor(clazz, () -> context().actorOf(props, name));
    }

    private ActorRef registerActor(final Class<?> clazz, final Creator<ActorRef> creator) {
        try {
            final ActorRef ref = creator.create();
            context().watch(ref);
            children.put(clazz, ref);
            factories.put(clazz, creator);
            classes.put(ref, clazz);
            heartbeats.put(ref, System.currentTimeMillis());
            return ref;
        } catch (Exception e) {
            logger.error("Failed to create actor {}", clazz, e);
            return null;
        }
    }

    private void stopActor(final ActorRef ref) {
        context().stop(ref);
        context().unwatch(ref);
        heartbeats.remove(ref);
    }

    // API
    public static Props props(final JobService jobService, final TaskService taskService, final FileStorageService fsService, final TaskExecutor executor) {
        return Props.create(SupervisorActor.class, () -> new SupervisorActor(jobService, taskService, fsService, executor));
    }

    // MESSAGES
    @Data
    public static class Forward {

        private final Class<?> clazz;
        private final Object message;

    }

}
