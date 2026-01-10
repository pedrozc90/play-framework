package controllers.files.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import models.jobs.Job;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class JobDto extends AuditEntityDto implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "status")
    private Job.Status status;

    @JsonProperty(value = "file")
    private FileStorageDto file;

}
