package controllers.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDto {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "quantity", required = true)
    private Integer quantity = 0;

    @JsonProperty(value = "label_type", required = true)
    private String labelType;

    @JsonProperty(value = "product", required = true)
    private ProductDto product;

}
