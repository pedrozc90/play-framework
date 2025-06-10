package controllers.files.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class FileStorageDto extends AuditEntityDto implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "hash")
    private String hash;

    @JsonProperty(value = "filename")
    private String filename;

    @JsonProperty(value = "content_type")
    private String contentType;

    @JsonProperty(value = "charset")
    private String charset;

    @JsonProperty(value = "length")
    private long length;

}
