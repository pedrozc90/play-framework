package repositories;

import core.persistence.DatabaseExecutionContext;
import core.persistence.JPARepositoryImpl;
import models.tasks.Task;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TaskRepository extends JPARepositoryImpl<Task, Long> {

    @Inject
    public TaskRepository(final JPAApi jpaApi, final DatabaseExecutionContext context) {
        super(Task.class, jpaApi, context);
    }

}
