package web.controllers.files;

import application.files.FileStorageService;
import core.exceptions.AppException;
import core.objects.FileMetadata;
import core.objects.Page;
import core.play.utils.ResultBuilder;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.Files.TemporaryFile;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import web.dtos.FileStorageDto;
import web.mappers.FileStorageMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class FileController extends Controller {

    private static final Logger.ALogger logger = Logger.of(FileController.class);

    private final FileStorageService fsService;
    private final FileStorageMapper mapper;

    @Inject
    public FileController(
        final FileStorageService fsService,
        final FileStorageMapper mapper
    ) {
        this.fsService = fsService;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public CompletionStage<Result> fetch(final int page, final int rows, final String q) throws AppException {
        return fsService.fetch(page, rows, q).thenApply((res) -> {
            final Page<FileStorageDto> resultDto = res.map(mapper::toDto);
            return ResultBuilder.of(resultDto).ok();
        });
    }

    public CompletionStage<Result> upload(final Http.Request request) throws AppException {
        final Http.MultipartFormData<TemporaryFile> payload = request.body().asMultipartFormData();
        Objects.requireNonNull(payload, "Multipart form data must not be null");

        final Http.MultipartFormData.FilePart<TemporaryFile> part = payload.getFile("file");

        final FileMetadata metadata = FileMetadata.of(part);

        return CompletableFuture.supplyAsync(() -> {
            try {
                fsService.upload(metadata);
                return ResultBuilder.of("File successfully uploaded.").ok();
            } catch (Throwable e) {
                throw AppException.of(e, "Upload failed.").toCompletionException();
            }
        });
    }

}
