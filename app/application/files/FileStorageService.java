package application.files;

import actors.ActorsManager;
import application.jobs.JobService;
import core.objects.FileMetadata;
import core.objects.Page;
import core.utils.FileUtils;
import core.utils.HashUtils;
import domain.files.FileStorage;
import domain.jobs.Job;
import infrastructure.repositories.files.FileStorageRepository;
import play.Logger;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class FileStorageService {

    private final Logger.ALogger logger = Logger.of(FileStorageService.class);

    private final JPAApi jpa;
    private final FileStorageRepository repository;
    private final JobService jobService;
    private final ActorsManager actorsManager;

    @Inject
    public FileStorageService(
        final JPAApi jpa,
        final FileStorageRepository repository,
        final JobService jobService,
        final ActorsManager actorsManager
    ) {
        this.jpa = jpa;
        this.repository = repository;
        this.jobService = jobService;
        this.actorsManager = actorsManager;
    }

    // QUERY
    public CompletionStage<FileStorage> get(final UUID uuid) {
        return CompletableFuture.supplyAsync(() -> repository.wrap((em) -> repository.get(em, uuid)));
    }

    // METHODS
    public FileStorage create(final EntityManager em, final String filename, final byte[] bytes, final String contentType, final String charset) {
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

        return repository.persist(em, obj);
    }

    public FileStorage create(final EntityManager em, final String filename, final byte[] bytes) {
        final String contentType = FileUtils.getContentType(filename);
        final Charset charset = FileUtils.isText(contentType) ? StandardCharsets.UTF_8 : null;
        return create(em, filename, bytes, contentType, charset != null ? charset.name() : null);
    }

    // ENDPOINTS
    public CompletableFuture<Page<FileStorage>> fetch(final int page, final int rows, final String q) {
        return CompletableFuture.supplyAsync(() -> repository.wrap((em) -> repository.fetch(em, page, rows, q)));
    }

    public void upload(final FileMetadata metadata) {
        final Job j = jpa.withTransaction((em) -> {
            final FileStorage fs = this.create(em, metadata.getFilename(), metadata.getBytes(), metadata.getContentType(), null);
            logger.info("File {} (hash: {}) persisted.", fs.getFilename(), fs.getHash());

            final Job job = jobService.create(em, fs);
            logger.info("New pending job (id: {}) for file {}.", job.getId(), fs.getFilename());

            return job;
        });

        // notify worker **after** commit
        actorsManager.queue(j);
    }

}
