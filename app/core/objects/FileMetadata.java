package core.objects;

import core.exceptions.AppException;
import core.utils.FileUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import play.mvc.Http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Data
@EqualsAndHashCode
@ToString
public class FileMetadata {

    private final String filename;
    private final byte[] bytes;
    private final String contentType;

    public static FileMetadata of(final String filename, final byte[] bytes) {
        final String contentType = FileUtils.getContentType(filename);
        return new FileMetadata(filename, bytes, contentType);
    }

    public static FileMetadata of(final Http.MultipartFormData.FilePart<File> part) throws AppException {
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
