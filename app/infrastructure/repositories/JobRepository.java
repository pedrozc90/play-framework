package infrastructure.repositories;

import domain.files.FileStorage;
import domain.jobs.Job;

import javax.persistence.NoResultException;

public class JobRepository extends JpaRepository<Job, Long> {

    private static JobRepository instance;

    public static JobRepository getInstance() {
        if (instance == null) {
            instance = new JobRepository();
        }
        return instance;
    }

    public JobRepository() {
        super(Job.class);
    }

    public Job get(final FileStorage file) {
        try {
            return em().createQuery("SELECT j FROM Job j WHERE j.file.id = :file_id", Job.class)
                .setParameter("file_id", file.getId())
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
