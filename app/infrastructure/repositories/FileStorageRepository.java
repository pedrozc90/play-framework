package infrastructure.repositories;

import com.querydsl.jpa.impl.JPAQuery;
import core.objects.Page;
import domain.files.FileStorage;
import domain.files.QFileStorage;

import java.util.UUID;

public class FileStorageRepository extends JpaRepository<FileStorage, QFileStorage, Long> {

    private static FileStorageRepository instance;

    public static FileStorageRepository getInstance() {
        if (instance == null) {
            instance = new FileStorageRepository();
        }
        return instance;
    }

    public FileStorageRepository() {
        super(FileStorage.class, QFileStorage.fileStorage);
    }

    public FileStorage get(final UUID uuid) {
        if (uuid == null) return null;
        return createQuery()
            .where(entity.uuid.eq(uuid.toString()))
            .fetchOne();
    }

    public Page<FileStorage> fetch(final int page, final int rows, final String q) {
        final JPAQuery<FileStorage> query = createQuery();

        if (q != null) {
            query.where(entity.filename.contains(q));
        }

        query.orderBy(entity.filename.asc());

        return fetch(query, page, rows);
    }

}
