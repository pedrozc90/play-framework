package application.jobs;

import domain.files.FileStorage;
import domain.jobs.Job;
import infrastructure.repositories.jobs.JobRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class JobService {

    @Inject
    private JobRepository repository;

    // QUERY
    public Job get(final EntityManager em, final Long id) {
        return repository.findById(em, id);
    }

    // METHODS
    public Job create(final EntityManager em, final FileStorage file) {
        final Job obj = new Job();
        obj.setStatus(Job.Status.PENDING);
        obj.setFile(file);

        return repository.persist(em, obj);
    }

}
