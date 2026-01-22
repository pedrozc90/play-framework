package web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import core.utils.jackson.TimestampDeserializer;
import core.utils.jackson.TimestampSerializer;
import domain.tasks.TaskStatus;
import domain.tasks.TaskType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    @JsonProperty(value = "stack_trace")
    private final String stackTrace;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @JsonProperty(value = "started_at")
    private final Instant startedAt;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
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
        final String stackTrace,
        final Instant startedAt,
        final Instant completedAt
    ) {
        super(uuid, insertedAt, updatedAt, version);
        this.id = id;
        this.type = type;
        this.status = status;
        this.stackTrace = stackTrace;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

}
