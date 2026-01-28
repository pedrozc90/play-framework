package infrastructure.repositories.jobs;

import com.google.inject.ImplementedBy;
import domain.files.FileStorage;
import domain.jobs.Job;
import infrastructure.repositories.JpaRepository;

import javax.persistence.EntityManager;

@ImplementedBy(JobRepositoryImpl.class)
public interface JobRepository extends JpaRepository<Job> {

    Job findById(final EntityManager em, final Long id);

    Job get(final EntityManager em, final FileStorage file);
}
