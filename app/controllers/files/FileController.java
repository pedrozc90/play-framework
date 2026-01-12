package controllers.files;

import actors.ActorsManager;
import core.exceptions.AppException;
import core.objects.FileMetadata;
import core.utils.objects.ResultBuilder;
import models.files.FileStorage;
import models.jobs.Job;
import play.Logger;
import play.db.jpa.JPAApi;
import play.libs.Files.TemporaryFile;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.FileStorageService;
import services.JobService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

@Singleton
public class FileController extends Controller {

    private static final Logger.ALogger logger = Logger.of(FileController.class);

    private final HttpExecutionContext context;
    private final JPAApi jpa;
    private final FileStorageService fsService;
    private final JobService jobService;
    private final ActorsManager actorsManager;

    @Inject
    public FileController(final HttpExecutionContext context,
                          final JPAApi jpa,
                          final FileStorageService fsService,
                          final JobService jobService,
                          final ActorsManager actorsManager) {
        this.context = context;
        this.jpa = jpa;
        this.fsService = fsService;
        this.jobService = jobService;
        this.actorsManager = actorsManager;
    }

    public CompletionStage<Result> upload(final Http.Request request) {
        return CompletableFuture
            .supplyAsync(
                () -> {
                    try {
                        final Http.MultipartFormData<TemporaryFile> payload = request.body().asMultipartFormData();
                        Objects.requireNonNull(payload, "Multipart form data must not be null");

                        final Http.MultipartFormData.FilePart<TemporaryFile> part = payload.getFile("file");
                        Objects.requireNonNull(part, "'file' must not be null");

                        return FileMetadata.of(part);
                    } catch (Throwable e) {
                        throw new CompletionException(AppException.of(e, "Upload failed."));
                    }
                },
                context.current()
            ).thenApply((metadata) -> {
                final Job persistedJob = jpa.withTransaction((em) -> {
                    final FileStorage fs = fsService.create(em, metadata.getFilename(), metadata.getBytes(), metadata.getContentType(), null);
                    logger.info("File {} (hash: {}) persisted.", fs.getFilename(), fs.getHash());

                    final Job job = jobService.create(em, fs);
                    logger.info("New pending job (id: {}) for file {}.", job.getId(), fs.getFilename());

                    return job;
                });

                // notify worker (**after** commit)
                actorsManager.queue(persistedJob);

                return ResultBuilder.of("File successfully uploaded.").ok();
            });
    }

}
