package services;

import core.utils.FileUtils;
import core.utils.HashUtils;
import models.files.FileStorage;
import repositories.FileStorageRepository;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class FileStorageService {

    private static FileStorageService instance;

    private final FileStorageRepository repository = FileStorageRepository.getInstance();

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
        final String hash = HashUtils.sha256(bytes);

        final String extension = FileUtils.getExtension(filename);

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
        final String contentType = FileUtils.getContentType(filename);
        final Charset charset = FileUtils.isText(contentType) ? StandardCharsets.UTF_8 : null;
        return create(filename, bytes, contentType, charset != null ? charset.name() : null);
    }

}
