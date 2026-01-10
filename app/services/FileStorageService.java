package services;

import core.utils.FileUtils;
import core.utils.HashUtils;
import models.files.FileStorage;
import repositories.FileStorageRepository;

import java.util.UUID;

public class FileStorageService {

    private static FileStorageService instance;

    private final FileStorageRepository repository = FileStorageRepository.getInstance();

    private final HashUtils hashUtils = HashUtils.getInstance();
    private final FileUtils fileUtils = FileUtils.getInstance();

    public static FileStorageService getInstance() {
        if (instance == null) {
            instance = new FileStorageService();
        }
        return instance;
    }

    // QUERY
    public FileStorage get(final UUID uuid) {
        return repository.get(uuid);
    }

    // METHODS
    public FileStorage create(final String filename, final byte[] bytes, final String contentType, final String charset) {
        final String hash = hashUtils.sha256(bytes);

        final String extension = fileUtils.getExtension(filename);

        final FileStorage obj = new FileStorage();
        obj.setFilename(filename);
        obj.setHash(hash);
        obj.setContent(bytes);
        obj.setContentType(contentType);
        obj.setExtension(extension);
        obj.setCharset(charset);
        obj.setLength(bytes.length);

        return repository.persist(obj);
    }

    public FileStorage create(final String filename, final byte[] bytes) {
        return create(filename, bytes, "text/plain", null);
    }

}
