package services;

import actors.ActorsManager;
import models.files.FileStorage;
import models.jobs.Job;
import repositories.JobRepository;

public class JobService {

    private final JobRepository repository = JobRepository.getInstance();
    private final ActorsManager actors = ActorsManager.getInstance();

    private static JobService instance;

    public static JobService getInstance() {
        if (instance == null) {
            instance = new JobService();
        }
        return instance;
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
