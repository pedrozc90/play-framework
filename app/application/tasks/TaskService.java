package application.tasks;


import application.files.FileStorageService;
import core.objects.FileMetadata;
import domain.files.FileStorage;
import domain.jobs.Job;
import domain.tasks.Task;
import domain.tasks.TaskStatus;
import domain.tasks.TaskType;
import infrastructure.repositories.tasks.TaskRepository;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class TaskService {

    private final JPAApi jpa;
    private final TaskRepository repository;
    private final FileStorageService fsService;

    @Inject
    public TaskService(
        final JPAApi jpa,
        final TaskRepository repository,
        final FileStorageService fsService
    ) {
        this.jpa = jpa;
        this.repository = repository;
        this.fsService = fsService;
    }

    // QUERY
    public Task get(final EntityManager em, final Long id) {
        return repository.findById(em, id);
    }

    // METHOD
    public Task create(final EntityManager em, final TaskType type, final Job job) {
        return create(em, type, TaskStatus.NEW, null, null, job);
    }

    public Task create(final EntityManager em, final TaskType type, final TaskStatus status, final Timestamp startedAt, final Timestamp completedAt, final Job job) {
        final Task obj = new Task();
        obj.setType(type);
        obj.setStatus(status);
        obj.setStartedAt(startedAt);
        obj.setCompletedAt(completedAt);
        obj.setJob(job);
        return repository.persist(em, obj);
    }

    public void update(final EntityManager em, final Long id, final TaskStatus status) {
        final Task task = repository.findById(em, id);
        task.setStatus(status);
        // TODO: do we need to persist ???
        // repository.persist(task);
    }

    public List<Task> generateAll(final EntityManager em, final Job job) {
        final FileStorage fs = job.getFile();
        final String extension = fs.getExtension();

        // return Arrays.stream(TaskType.values())
        return Stream.of(TaskType.RESIZE_TEST)
            .filter(type -> generateFilter(type, extension))
            .map((type) -> create(em, type, job))
            .collect(Collectors.toList());
    }

    private boolean generateFilter(final TaskType type, final String extension) {
        if (extension.equals("png") && type == TaskType.CONVERT_PNG) {
            return false;
        } else if (extension.equals("jpeg") && type == TaskType.CONVERT_JPEG) {
            return false;
        } else if (extension.equals("jpg") && type == TaskType.CONVERT_JPG) {
            return false;
        }
        return true;
    }

    public FileStorage completeTask(final Long taskId, final TaskStatus status, final FileMetadata result) {
        return jpa.withTransaction((em) -> {
            this.update(em, taskId, status);
            if (result != null) {
                return fsService.create(em, result.getFilename(), result.getBytes());
            }
            return null;
        });
    }

    public FileStorage completeTask(final Long taskId, final TaskStatus status) {
        return completeTask(taskId, status, null);
    }

}
