package web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import domain.jobs.Job;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JobDto extends AuditEntityDto implements Serializable {

    @JsonProperty(value = "id")
    private final Long id;

    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "status")
    private final Job.Status status;

    @JsonProperty(value = "file")
    private final FileStorageDto file;

    public JobDto(
        final UUID uuid,
        final Instant insertedAt,
        final Instant updatedAt,
        final Integer version,
        final Long id,
        final Job.Status status,
        final FileStorageDto file
    ) {
        super(uuid, insertedAt, updatedAt, version);
        this.id = id;
        this.status = status;
        this.file = file;
    }

}
