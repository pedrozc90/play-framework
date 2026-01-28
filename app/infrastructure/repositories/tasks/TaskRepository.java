package infrastructure.repositories.tasks;

import com.google.inject.ImplementedBy;
import domain.tasks.Task;
import infrastructure.repositories.JpaRepository;

import javax.persistence.EntityManager;

@ImplementedBy(TaskRepositoryImpl.class)
public interface TaskRepository extends JpaRepository<Task> {

    Task findById(final EntityManager em, final Long id);
}
