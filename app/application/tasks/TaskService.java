package application.tasks;


import domain.files.FileStorage;
import domain.jobs.Job;
import domain.tasks.Task;
import domain.tasks.TaskStatus;
import domain.tasks.TaskType;
import infrastructure.repositories.TaskRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskService {

    private final TaskRepository repository = TaskRepository.getInstance();

    private static TaskService instance;

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    // QUERY
    public Task get(final Long id) {
        return repository.findById(id);
    }

    // METHOD
    public Task create(final TaskType type, final Job job) {
        return create(type, TaskStatus.NEW, null, null, job);
    }

    public Task create(final TaskType type, final TaskStatus status, final Timestamp startedAt, final Timestamp completedAt, final Job job) {
        final Task obj = new Task();
        obj.setType(type);
        obj.setStatus(status);
        obj.setStartedAt(startedAt);
        obj.setCompletedAt(completedAt);
        obj.setJob(job);
        return repository.persist(obj);
    }

    public void update(final Long id, final TaskStatus status) {
        final Task task = repository.findById(id);
        task.setStatus(status);
        // TODO: do we need to persist ???
        // repository.persist(task);
    }

    public List<Task> generateAll(final Job job) {
        final FileStorage fs = job.getFile();
        final String extension = fs.getExtension();

        // return Arrays.stream(TaskType.values())
        return Stream.of(TaskType.RESIZE_TEST)
            .filter(type -> generateFilter(type, extension))
            .map((type) -> create(type, job))
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
