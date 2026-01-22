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
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import web.dtos.FileStorageDto;
import web.mappers.FileStorageMapper;

import java.util.Objects;

public class FileController extends Controller {

    private static final Logger.ALogger logger = Logger.of(FileController.class);

    private static final FileStorageService fsService = FileStorageService.getInstance();
    private static final JobService jobService = JobService.getInstance();
    private static final ActorsManager actorsManager = ActorsManager.getInstance();
    private static final FileStorageMapper mapper = FileStorageMapper.getInstance();

    public static Result fetch(final int page, final int rows, final String q) throws AppException {
        final Page<FileStorage> result = fsService.fetch(page, rows, q);
        final Page<FileStorageDto> resultDto = result.map(mapper::toDto);
        return ResultBuilder.of(resultDto).ok();
    }

    public static Result upload() throws AppException {
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
