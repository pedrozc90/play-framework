package controllers.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class OrderDto implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "number", required = true)
    private String number;

    @JsonProperty(value = "status", required = true)
    private String status;

    @JsonProperty(value = "supplier")
    private SupplierDto supplier;

    @JsonProperty(value = "items", required = true)
    private Set<OrderItemDto> items;

}
