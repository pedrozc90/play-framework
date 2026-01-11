package core.objects;

import lombok.Data;

@Data
public class FileMetadata {

    private final String filename;
    private final byte[] bytes;
    private final String contentType;

}
