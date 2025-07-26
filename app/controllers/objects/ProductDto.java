package controllers.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ProductDto implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "ean", required = true)
    private String ean;

    @JsonProperty(value = "description", required = true)
    private String description;

    @JsonProperty(value = "size")
    private String size;

    @JsonProperty(value = "color")
    private String color;

}
