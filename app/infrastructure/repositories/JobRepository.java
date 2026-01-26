package infrastructure.repositories;

import domain.files.FileStorage;
import domain.jobs.Job;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.NoResultException;

@Singleton
public class JobRepository extends JpaRepository<Job, Long> {

        @Inject
    public JobRepository(final JPAApi jpa) {
        super(jpa, Job.class);
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
