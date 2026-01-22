package actors.files;

import actors.messages.Init;
import actors.shared.BaseActor;
import actors.tasks.ConvertImageTask;
import actors.tasks.ResizeImageTask;
import actors.tasks.TaskExecutor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import application.files.FileStorageService;
import application.tasks.TaskService;
import core.objects.FileMetadata;
import domain.files.FileStorage;
import domain.tasks.TaskStatus;
import domain.tasks.TaskType;
import lombok.Data;
import play.db.jpa.JPA;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.concurrent.RecursiveTask;

public class FileProcessorActor extends BaseActor {

    private static final int MAX_RETRIES = 5;
    private final TaskService taskService = TaskService.getInstance();
    private final FileStorageService fsService = FileStorageService.getInstance();
    private final TaskExecutor executor = TaskExecutor.getInstance();

    private final ActorRef dispatcher;

    private FileProcessorActor(final ActorRef dispatcher) {
        super();
        this.dispatcher = dispatcher;
    }

    // RECEIVER
    @Override
    protected PartialFunction<Object, BoxedUnit> createReceive() {
        return ReceiveBuilder
            .match(Init.class, this::onInit)
            .match(Command.class, this::onCommand)
            .matchAny(this::onUnknownMessage)
            .build();
    }

    // TODO: Method Not Implemented
    private void onCommand(final Command cmd) {
        try {
            logger.info("Received command: {}", cmd);

            final RecursiveTask<FileMetadata> task = cmd.type.isResize()
                ? new ResizeImageTask(cmd.bytes, cmd.filename, cmd.type.getWidth(), cmd.type.getHeigth())
                : new ConvertImageTask(cmd.bytes, cmd.filename, cmd.extension);

            final FileMetadata result = executor.execute(task);

            JPA.withTransaction(() -> {
                taskService.update(cmd.taskId, TaskStatus.DONE);

                final FileStorage fs = fsService.create(result.getFilename(), result.getBytes());
                logger.info("New file storage created: {}", fs);
            });
        } catch (Exception e) {
            int retries = cmd.retries + 1;
            if (retries > MAX_RETRIES) {
                logger.error("Failed to process command: {}", cmd);
                JPA.withTransaction(() -> taskService.update(cmd.taskId, TaskStatus.FAILED));
            } else {
                dispatcher.tell(cmd.withRetries(retries), self());
            }
        }
    }

    // API
    public static Props props(final ActorRef dispatcher) {
        return Props.create(FileProcessorActor.class, () -> new FileProcessorActor(dispatcher));
    }

    // MESSAGES
    @Data
    public static class Command {

        private final Long taskId;
        private final TaskType type;
        private final byte[] bytes;
        private final String filename;
        private final String extension;
        private final int retries;

        public Command(final Long taskId, final TaskType type, final byte[] bytes, final String filename, final String extension, final int retries) {
            this.taskId = taskId;
            this.type = type;
            this.bytes = bytes;
            this.filename = filename;
            this.extension = extension;
            this.retries = retries;
        }

        public Command(final Long taskId, final TaskType type, final byte[] bytes, final String filename, final String extension) {
            this(taskId, type, bytes, filename, extension, 0);
        }

        public Command withRetries(int r) {
            return new Command(taskId, type, bytes, filename, extension, r);
        }

        public Command withoutRetries() {
            return new Command(taskId, type, bytes, filename, extension, 0);
        }

    }

}
