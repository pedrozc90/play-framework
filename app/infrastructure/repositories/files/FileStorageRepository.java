package infrastructure.repositories.files;

import com.google.inject.ImplementedBy;
import core.objects.Page;
import domain.files.FileStorage;
import infrastructure.repositories.JpaRepository;

import javax.persistence.EntityManager;
import java.util.UUID;

@ImplementedBy(FileStorageRepositoryImpl.class)
public interface FileStorageRepository extends JpaRepository<FileStorage> {

    FileStorage get(final EntityManager em, final UUID uuid);

    Page<FileStorage> fetch(final EntityManager em, final int page, final int rows, final String q);

}
