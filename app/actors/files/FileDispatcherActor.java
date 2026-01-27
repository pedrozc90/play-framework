package actors.files;

import actors.messages.Heartbeat;
import actors.messages.HeartbeatAck;
import actors.messages.Init;
import actors.shared.BaseActor;
import actors.tasks.TaskExecutor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import application.tasks.FileDispatcherService;
import application.tasks.TaskService;
import lombok.Data;

import java.util.Collections;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileDispatcherActor extends BaseActor {

    //    private final JobService jobService;
    private final TaskService taskService;
    private final TaskExecutor executor;
    private final FileDispatcherService service;

    private final Queue<FileProcessorActor.Command> queue = new ConcurrentLinkedQueue<>();
    private final Set<FileProcessorActor.Command> actives = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final int parallelism;
    private ActorRef router;

    public FileDispatcherActor(
//        final JobService jobService,
        final TaskService taskService,
        final TaskExecutor executor,
        final FileDispatcherService service,
        final int parallelism
    ) {
        super();
//        this.jobService = jobService;
        this.taskService = taskService;
        this.executor = executor;
        this.service = service;
        this.parallelism = parallelism;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(Init.class, this::onInit)
            .match(Enqueue.class, this::onEnqueue)
            .match(Dequeue.class, this::onDequeue)
            .match(ScheduleNext.class, this::onScheduleNext)
            .match(Heartbeat.class, this::onHeartbeat)
            .matchAny(this::onUnknownMessage)
            .build();
    }

    @Override
    protected void onInit(final Init obj) {
        super.onInit(obj);
        this.router = context().actorOf(new RoundRobinPool(parallelism).props(FileProcessorActor.props(taskService, executor, self())));
    }

    private void onEnqueue(final Enqueue cmd) {
        logger.info("Received enqueue command: {}", cmd);
        service.completeJob(cmd.jobId, queue);
        self().tell(new ScheduleNext(), self());
    }

    private void onDequeue(final Dequeue cmd) {
        logger.info("Received dequeue command {}", cmd);
    }

    private void onScheduleNext(final ScheduleNext obj) {
        final int available = parallelism - actives.size();
        if (available > 0) {
            logger.info("Scheduling up to {} task(s)", available);
            for (int index = 0; index < available && !queue.isEmpty(); index++) {
                final FileProcessorActor.Command command = queue.poll();
                if (command == null) break;
                actives.add(command);
                router.tell(command, self());
            }
        } else {
            logger.info("No available workers slots ({} actives)", actives.size());
        }
    }

    @Override
    protected void onHeartbeat(final Heartbeat obj) {
        super.onHeartbeat(obj);
        final ActorRef parent = context().parent();
        parent.tell(new HeartbeatAck(FileDispatcherActor.class, self()), self());
    }

    // PROPS
    public static Props props(final TaskService taskService, final TaskExecutor executor, final FileDispatcherService service, final int parallelism) {
        return Props.create(FileDispatcherActor.class, () -> new FileDispatcherActor(taskService, executor, service, parallelism));
    }

    @Data
    public static class Enqueue {

        private final Long jobId;

        public Enqueue(final Long jobId) {
            this.jobId = jobId;
        }

    }

    @Data
    public static class Dequeue {

        private final FileProcessorActor.Command command;
        private final Throwable cause;

        public Dequeue(final FileProcessorActor.Command command, final Throwable cause) {
            this.command = command;
            this.cause = cause;
        }

    }

    public static class ScheduleNext {
        // blank
    }

}
