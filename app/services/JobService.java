package services;

import actors.ActorsManager;
import models.files.FileStorage;
import models.jobs.Job;
import repositories.JobRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    public Job get(final Long id) {
        return repository.findById(id);
    }

    public Job getByFile(final FileStorage file) {
        return repository.get(file);
    }

    // METHODS
    public Job create(final FileStorage file) {
        final Job obj = new Job();
        obj.setStatus(Job.Status.PENDING);
        obj.setFile(file);

        final Job persisted = repository.persist(obj);

        // add job to processing queue
        actors.queue(persisted);

        return persisted;
    }

}
