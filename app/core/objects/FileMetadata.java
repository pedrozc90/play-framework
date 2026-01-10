package core.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileMetadata {

    private final String filename;
    private final byte[] bytes;
    private final String contentType;

}
