package infrastructure.repositories;

import domain.tasks.QTask;
import domain.tasks.Task;

public class TaskRepository extends JpaRepository<Task, QTask, Long> {

    private static TaskRepository instance;

    public static TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    public TaskRepository() {
        super(Task.class, QTask.task);
    }

}
