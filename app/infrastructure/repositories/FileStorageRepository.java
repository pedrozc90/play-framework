package infrastructure.repositories;

import domain.files.FileStorage;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
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
            return em().createQuery("SELECT fs FROM FileStorage fs WHERE fs.uuid = :uuid", FileStorage.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<FileStorage> fetch(final int page, final int rows, final String q) {
        String text = "SELECT fs FROM FileStorage fs";
        if (q != null) {
            text += " WHERE fs.filename LIKE :q";
        }
        text += " LIMIT :limit OFFSET :offset";

        Query query = em().createQuery(text);
        if (q != null) {
            query.setParameter("q", "%" + q + "%");
        }

        return query.setParameter("limit", rows)
            .setParameter("offset", (page - 1) * rows)
            .getResultList();
    }

}
