package repositories;

import models.files.FileStorage;
import play.db.jpa.JPA;

import javax.persistence.NoResultException;
import java.util.UUID;

public class FileStorageRepository extends JpaRepository<FileStorage, Long> {

    private static FileStorageRepository instance;

    public static FileStorageRepository getInstance() {
        if (instance == null) {
            instance = new FileStorageRepository();
        }
        return instance;
    }

    public FileStorageRepository() {
        super(FileStorage.class);
    }

    public FileStorage get(final UUID uuid) {
        try {
            return JPA.em().createQuery("SELECT fs FROM FileStorage fs WHERE fs.uuid = :uuid", FileStorage.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        }  catch (NoResultException e) {
            return null;
        }
    }

}
