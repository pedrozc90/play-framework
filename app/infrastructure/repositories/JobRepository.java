package infrastructure.repositories;

import domain.files.FileStorage;
import domain.jobs.Job;
import domain.jobs.QJob;

public class JobRepository extends JpaRepository<Job, QJob, Long> {

    private static JobRepository instance;

    public static JobRepository getInstance() {
        if (instance == null) {
            instance = new JobRepository();
        }
        return instance;
    }

    public JobRepository() {
        super(Job.class, QJob.job);
    }

    public Job get(final FileStorage file) {
        if (file == null) return null;
        return createQuery()
            .where(entity.file.eq(file))
            .fetchOne();
    }

}
