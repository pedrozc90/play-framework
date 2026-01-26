package application.jobs;

import domain.files.FileStorage;
import domain.jobs.Job;
import infrastructure.repositories.JobRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JobService {

    @Inject
    private JobRepository repository;

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

        return repository.persist(obj);
    }

}
