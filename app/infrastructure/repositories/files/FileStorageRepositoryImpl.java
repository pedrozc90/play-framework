package infrastructure.repositories.files;

import core.objects.Page;
import domain.files.FileStorage;
import infrastructure.repositories.DatabaseExecutionContext;
import infrastructure.repositories.JpaRepositoryImpl;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public class FileStorageRepositoryImpl extends JpaRepositoryImpl<FileStorage, Long> implements FileStorageRepository {

    @Inject
    public FileStorageRepositoryImpl(final JPAApi jpa, final DatabaseExecutionContext context) {
        super(jpa, context, FileStorage.class);
    }

    @Override
    public FileStorage get(final EntityManager em, final UUID uuid) {
        try {
            return em.createQuery("SELECT fs FROM FileStorage fs WHERE fs.uuid = :uuid", FileStorage.class)
                .setParameter("uuid", uuid)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Page<FileStorage> fetch(final EntityManager em, final int page, final int rows, final String q) {
        String where = "WHERE 1 = 1";
        final Map<String, Object> params = new HashMap<>();

        if (q != null && !q.isEmpty()) {
            where += " AND fs.filename LIKE :q";
            params.put("q", "%" + q + "%");
        }

        final TypedQuery<FileStorage> listQuery = em.createQuery("SELECT fs FROM FileStorage fs " + where + " ORDER BY fs.filename", FileStorage.class);
        final TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(*) FROM FileStorage fs " + where, Long.class);

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
