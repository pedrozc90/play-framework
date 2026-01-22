package web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileStorageDto extends AuditEntityDto implements Serializable {

    @JsonProperty(value = "id")
    private final Long id;

    @JsonProperty(value = "hash")
    private final String hash;

    @JsonProperty(value = "filename")
    private final String filename;

    @JsonProperty(value = "content_type")
    private final String contentType;

    @JsonProperty(value = "charset")
    private final String charset;

    @JsonProperty(value = "length")
    private final long length;

    public FileStorageDto(
        final UUID uuid,
        final Instant insertedAt,
        final Instant updatedAt,
        final Integer version,
        final Long id,
        final String hash,
        final String filename,
        final String contentType,
        final String charset,
        final long length
    ) {
        super(uuid, insertedAt, updatedAt, version);
        this.id = id;
        this.hash = hash;
        this.filename = filename;
        this.contentType = contentType;
        this.charset = charset;
        this.length = length;
    }

}
