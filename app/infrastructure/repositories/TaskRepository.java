package infrastructure.repositories;

import domain.tasks.Task;

public class TaskRepository extends JpaRepository<Task, Long> {

    private static TaskRepository instance;

    public static TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    public TaskRepository() {
        super(Task.class);
    }

}
