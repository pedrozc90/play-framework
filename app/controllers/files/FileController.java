package controllers.files;

import actors.ActorsManager;
import core.exceptions.AppException;
import core.objects.FileMetadata;
import core.utils.objects.ResultBuilder;
import models.files.FileStorage;
import models.jobs.Job;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.FileStorageService;
import services.JobService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class FileController extends Controller {

    private static final Logger.ALogger logger = Logger.of(FileController.class);

    private static final FileStorageService fsService = FileStorageService.getInstance();
    private static final JobService jobService = JobService.getInstance();
    private static final ActorsManager actorsManager = ActorsManager.getInstance();

    public static Result upload() throws AppException {
        final Http.MultipartFormData payload = request().body().asMultipartFormData();
        Objects.requireNonNull(payload, "Multipart form data must not be null");

        final Http.MultipartFormData.FilePart part = payload.getFile("file");

        final FileMetadata metadata = parseMultipart(part);

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

    private static FileMetadata parseMultipart(final Http.MultipartFormData.FilePart part) throws AppException {
        Objects.requireNonNull(part, "'file' must not be null");

        final File file = part.getFile();
        final String filename = part.getFilename();
        final String contentType = part.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw AppException.of("Invalid content type: %s", contentType);
        }

        try {
            final byte[] bytes = Files.readAllBytes(file.toPath());
            return new FileMetadata(filename, bytes, contentType);
        } catch (IOException e) {
            throw AppException.of(e, "Unable to read file %s", filename);
        }
    }

}
