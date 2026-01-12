package services;

import models.files.FileStorage;
import models.jobs.Job;
import models.tasks.Task;
import models.tasks.TaskStatus;
import models.tasks.TaskType;
import repositories.TaskRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class TaskService {

    @Inject
    private TaskRepository repository;

    // QUERY
    public Task get(final EntityManager em, final Long id) {
        return repository.findById(em, id);
    }

    // METHOD
    public Task create(final EntityManager em, final TaskType type, final TaskStatus status, final Timestamp startedAt, final Timestamp completedAt, final Job job) {
        final Task obj = new Task();
        obj.setType(type);
        obj.setStatus(status);
        obj.setStartedAt(startedAt);
        obj.setCompletedAt(completedAt);
        obj.setJob(job);
        return repository.persist(em, obj);
    }

    public Task create(final EntityManager em, final TaskType type, final Job job) {
        return create(em, type, TaskStatus.NEW, null, null, job);
    }

    public void update(final EntityManager em, final Long id, final TaskStatus status) {
        final Task task = repository.findById(em, id);
        task.setStatus(status);
        repository.merge(em, task);
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

}
