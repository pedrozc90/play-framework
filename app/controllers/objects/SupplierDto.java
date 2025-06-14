package controllers.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SupplierDto implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "code", required = true)
    private String code;

}
