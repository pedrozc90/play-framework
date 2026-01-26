package infrastructure.repositories;

import domain.tasks.Task;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TaskRepository extends JpaRepository<Task, Long> {

    @Inject
    public TaskRepository(final JPAApi jpa) {
        super(jpa, Task.class);
    }

}
