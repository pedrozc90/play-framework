package services;

import actors.ActorsManager;
import models.files.FileStorage;
import models.jobs.Job;
import repositories.JobRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class JobService {

    private final JobRepository repository;
    private final ActorsManager actors;

    @Inject
    public JobService(final ActorsManager actors,
                      final JobRepository repository) {
        this.actors = actors;
        this.repository = repository;
    }

    // QUERY
    public Job get(final EntityManager em, final Long id) {
        return repository.findById(em, id);
    }

    public Job getByFile(final EntityManager em, final FileStorage file) {
        return repository.get(em, file);
    }

    // METHODS
    public Job create(final EntityManager em, final FileStorage file) {
        final Job obj = new Job();
        obj.setStatus(Job.Status.PENDING);
        obj.setFile(file);

        final Job persisted = repository.persist(em, obj);

        // add job to processing queue
        // actors.queue(persisted);

        return persisted;
    }

}
