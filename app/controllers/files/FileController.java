package controllers.files;

import actors.ActorsManager;
import core.exceptions.AppException;
import core.objects.FileMetadata;
import core.utils.objects.ResultBuilder;
import models.files.FileStorage;
import models.jobs.Job;
import play.Logger;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.FileStorageService;
import services.JobService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Singleton
public class FileController extends Controller {

    private static final Logger.ALogger logger = Logger.of(FileController.class);

    @Inject
    private FileStorageService fsService;

    @Inject
    private JobService jobService;

    @Inject
    private ActorsManager actorsManager;

    @Inject
    private JPAApi jpaApi;

    @Transactional
    public Result upload() throws AppException {
        final Http.MultipartFormData<File> payload = request().body().asMultipartFormData();
        Objects.requireNonNull(payload, "Multipart form data must not be null");

        final Http.MultipartFormData.FilePart<File> part = payload.getFile("file");

        final FileMetadata metadata = parseMultipart(part);

        try {
            final FileStorage fs = fsService.create(metadata.getFilename(), metadata.getBytes(), metadata.getContentType(), null);
            logger.info("File {} (hash: {}) persisted.", fs.getFilename(), fs.getHash());

            final Job job = jobService.create(fs);
            logger.info("New pending job (id: {}) for file {}.", job.getId(), fs.getFilename());

            // notify worker **after** commit
            actorsManager.queue(job);

            return ResultBuilder.of("File successfully uploaded.").ok();
        } catch (Throwable e) {
            throw AppException.of(e, "Upload failed.");
        }
    }

    private FileMetadata parseMultipart(final Http.MultipartFormData.FilePart<File> part) throws AppException {
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
