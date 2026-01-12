package repositories;

import core.persistence.DatabaseExecutionContext;
import core.persistence.JPARepositoryImpl;
import models.files.FileStorage;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.UUID;

@Singleton
public class FileStorageRepository extends JPARepositoryImpl<FileStorage, Long> {

    @Inject
    public FileStorageRepository(final JPAApi jpaApi, final DatabaseExecutionContext context) {
        super(FileStorage.class, jpaApi, context);
    }

    public FileStorage get(final EntityManager em, final UUID uuid) {
        try {
            return em.createQuery("SELECT fs FROM FileStorage fs WHERE fs.uuid = :uuid", FileStorage.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
