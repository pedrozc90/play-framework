package web.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
@MappedSuperclass
public abstract class AuditEntityDto implements Serializable {

    @JsonProperty(value = "uuid")
    private final UUID uuid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty(value = "inserted_at")
    private final Instant insertedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty(value = "updated_at")
    private final Instant updatedAt;

    @JsonProperty(value = "version")
    private final Integer version;

    public AuditEntityDto(final UUID uuid, final Instant insertedAt, final Instant updatedAt, final Integer version) {
        this.uuid = uuid;
        this.insertedAt = insertedAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

}
