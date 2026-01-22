package application.jobs;

import domain.files.FileStorage;
import domain.jobs.Job;
import infrastructure.repositories.JobRepository;

public class JobService {

    private final JobRepository repository = JobRepository.getInstance();

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

        return repository.persist(obj);
    }

}
