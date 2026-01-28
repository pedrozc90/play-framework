package infrastructure.repositories.tasks;

import domain.tasks.Task;
import infrastructure.repositories.DatabaseExecutionContext;
import infrastructure.repositories.JpaRepositoryImpl;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TaskRepositoryImpl extends JpaRepositoryImpl<Task, Long> implements TaskRepository {

    @Inject
    public TaskRepositoryImpl(final JPAApi jpa, final DatabaseExecutionContext context) {
        super(jpa, context, Task.class);
    }

}
