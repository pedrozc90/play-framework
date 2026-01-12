package repositories;

import core.persistence.DatabaseExecutionContext;
import core.persistence.JPARepositoryImpl;
import models.files.FileStorage;
import models.jobs.Job;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Singleton
public class JobRepository extends JPARepositoryImpl<Job, Long> {

    @Inject
    public JobRepository(final JPAApi jpaApi, final DatabaseExecutionContext context) {
        super(Job.class, jpaApi, context);
    }

    public Job get(final EntityManager em, final FileStorage file) {
        try {
            return em.createQuery("SELECT j FROM Job j WHERE j.file.id = :file_id", Job.class)
                .setParameter("file_id", file.getId())
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
