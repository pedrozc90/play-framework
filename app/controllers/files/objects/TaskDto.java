package controllers.files.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import models.tasks.TaskStatus;
import models.tasks.TaskType;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskDto extends AuditEntityDto implements Serializable {

    @JsonProperty(value = "id")
    private final Long id;

    @JsonProperty(value = "type")
    private final TaskType type;

    @JsonProperty(value = "status")
    private final TaskStatus status;

    @JsonProperty(value = "error_message")
    private final String errorMessage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty(value = "started_at")
    private final Instant startedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty(value = "completed_at")
    private final Instant completedAt;

    public TaskDto(
        final UUID uuid,
        final Instant insertedAt,
        final Instant updatedAt,
        final Integer version,
        final Long id,
        final TaskType type,
        final TaskStatus status,
        final String errorMessage,
        final Instant startedAt,
        final Instant completedAt
    ) {
        super(uuid, insertedAt, updatedAt, version);
        this.id = id;
        this.type = type;
        this.status = status;
        this.errorMessage = errorMessage;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

}
