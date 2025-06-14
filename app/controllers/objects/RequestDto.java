package controllers.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class RequestDto implements Serializable {

    @JsonProperty(value = "number", required = true)
    private String number;

    @JsonProperty(value = "supplier", required = true)
    private String supplier;

    @JsonProperty(value = "status", required = true)
    private String status;

    @JsonProperty(value = "items", required = true)
    private Set<RequestItemDto> items;

}
