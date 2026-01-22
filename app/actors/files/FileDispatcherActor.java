package actors.files;

import actors.messages.Heartbeat;
import actors.messages.HeartbeatAck;
import actors.messages.Init;
import actors.shared.BaseActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import akka.routing.RoundRobinPool;
import application.jobs.JobService;
import application.tasks.TaskService;
import domain.files.FileStorage;
import domain.jobs.Job;
import domain.tasks.Task;
import lombok.Data;
import play.db.jpa.JPA;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileDispatcherActor extends BaseActor {

    private final JobService jobService = JobService.getInstance();
    private final TaskService taskService = TaskService.getInstance();

    private final Queue<FileProcessorActor.Command> queue = new ConcurrentLinkedQueue<>();
    private final Set<FileProcessorActor.Command> actives = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final int parallelism;
    private ActorRef router;

    public FileDispatcherActor(final int parallelism) {
        super();
        this.parallelism = parallelism;
    }

    @Override
    protected PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(Init.class, this::onInit)
            .match(Enqueue.class, this::onEnqueue)
            .match(Dequeue.class, this::onDequeue)
            .match(ScheduleNext.class, this::onScheduleNext)
            .match(Heartbeat.class, this::onHeartbeat)
            .matchAny(this::onUnknownMessage)
            .build();
    }

    @Override
    protected void onInit(Init obj) {
        super.onInit(obj);
        this.router = context().actorOf(new RoundRobinPool(parallelism).props(FileProcessorActor.props(self())));
    }

    private void onEnqueue(final Enqueue cmd) {
        logger.info("Received enqueue command: {}", cmd);
        JPA.withTransaction(() -> {
            final Job job = jobService.get(cmd.jobId);
            job.setStatus(Job.Status.PROCESSING);

            final FileStorage fs = job.getFile();

            final List<Task> tasks = taskService.generateAll(job);

            for (Task task : tasks) {
                final FileProcessorActor.Command command = new FileProcessorActor.Command(task.getId(), task.getType(), fs.getContent(), fs.getFilename(), fs.getExtension());
                queue.add(command);
            }
        });

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
    public static Props props(final int parallelism) {
        return Props.create(FileDispatcherActor.class, () -> new FileDispatcherActor(parallelism));
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
