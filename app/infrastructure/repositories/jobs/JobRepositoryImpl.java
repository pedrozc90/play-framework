package infrastructure.repositories.jobs;

import domain.files.FileStorage;
import domain.jobs.Job;
import infrastructure.repositories.DatabaseExecutionContext;
import infrastructure.repositories.JpaRepositoryImpl;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Singleton
public class JobRepositoryImpl extends JpaRepositoryImpl<Job, Long> implements JobRepository {

    @Inject
    public JobRepositoryImpl(final JPAApi jpa, final DatabaseExecutionContext context) {
        super(jpa, context, Job.class);
    }

    @Override
    public Job get(final EntityManager em, final FileStorage file) {
        try {
            if (file == null) return null;
            return em.createQuery("SELECT j FROM Job j WHERE j.file.id = :file_id", Job.class)
                .setParameter("file_id", file.getId())
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
