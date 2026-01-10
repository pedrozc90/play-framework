package controllers.files.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import models.tasks.Task;
import models.tasks.TaskStatus;
import models.tasks.TaskType;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class TaskDto extends AuditEntityDto implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "type")
    private TaskType type;

    @JsonProperty(value = "status")
    private TaskStatus status;

    @JsonProperty(value = "error_message")
    private String errorMessage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty(value = "started_at")
    private Instant startedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty(value = "completed_at")
    private Instant completedAt;

}
