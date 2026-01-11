package repositories;

import core.persistence.JpaRepository;
import models.files.FileStorage;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.NoResultException;
import java.util.UUID;

@Singleton
public class FileStorageRepository extends JpaRepository<FileStorage, Long> {

    @Inject
    public FileStorageRepository(final JPAApi jpaApi) {
        super(FileStorage.class, jpaApi);
    }

    public FileStorage get(final UUID uuid) {
        try {
            return em().createQuery("SELECT fs FROM FileStorage fs WHERE fs.uuid = :uuid", FileStorage.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
