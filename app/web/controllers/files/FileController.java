package web.controllers.files;

import actors.ActorsManager;
import application.files.FileStorageService;
import application.jobs.JobService;
import core.exceptions.AppException;
import core.objects.FileMetadata;
import core.objects.Page;
import core.play.utils.ResultBuilder;
import domain.files.FileStorage;
import domain.jobs.Job;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import web.dtos.FileStorageDto;
import web.mappers.FileStorageMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class FileController extends Controller {

    private static final Logger.ALogger logger = Logger.of(FileController.class);

    private final FileStorageService fsService;
    private final JobService jobService;
    private final ActorsManager actorsManager;
    private final FileStorageMapper mapper;

    @Inject
    public FileController(
        final FileStorageService fsService,
        final JobService jobService,
        final ActorsManager actorsManager,
        final FileStorageMapper mapper
    ) {
        this.fsService = fsService;
        this.jobService = jobService;
        this.actorsManager = actorsManager;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Result fetch(final int page, final int rows, final String q) throws AppException {
        final Page<FileStorage> result = fsService.fetch(page, rows, q);
        final Page<FileStorageDto> resultDto = result.map(mapper::toDto);
        return ResultBuilder.of(resultDto).ok();
    }

    public Result upload() throws AppException {
        final Http.MultipartFormData payload = request().body().asMultipartFormData();
        Objects.requireNonNull(payload, "Multipart form data must not be null");

        final Http.MultipartFormData.FilePart part = payload.getFile("file");

        final FileMetadata metadata = FileMetadata.of(part);

        try {
            final Job j = JPA.withTransaction(() -> {
                final FileStorage fs = fsService.create(metadata.getFilename(), metadata.getBytes(), metadata.getContentType(), null);
                logger.info("File {} (hash: {}) persisted.", fs.getFilename(), fs.getHash());

                final Job job = jobService.create(fs);
                logger.info("New pending job (id: {}) for file {}.", job.getId(), fs.getFilename());

                return job;
            });

            // notify worker **after** commit
            actorsManager.queue(j);

            return ResultBuilder.of("File successfully uploaded.").ok();
        } catch (Throwable e) {
            throw AppException.of(e, "Upload failed.");
        }
    }

}
