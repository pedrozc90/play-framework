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

    public Page<FileStorage> fetch(final int page, final int rows, final String q) {
        String where = "WHERE 1 = 1";
        final Map<String, Object> params = new HashMap<>();

        if (q != null && !q.isEmpty()) {
            where += " AND fs.filename LIKE :q";
            params.put("q", "%" + q + "%");
        }

        final TypedQuery<FileStorage> listQuery = em().createQuery("SELECT fs FROM FileStorage fs " + where + " ORDER BY fs.filename", FileStorage.class);
        final TypedQuery<Long> countQuery = em().createQuery("SELECT COUNT(*) FROM FileStorage fs " + where, Long.class);

        params.forEach((k, v) -> {
            listQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        final Long total = countQuery.getSingleResult();
        final List<FileStorage> list = listQuery.setMaxResults(rows)
            .setFirstResult((page - 1) * rows)
            .getResultList();

        return new Page<>(page, rows, total, list);
    }

}
