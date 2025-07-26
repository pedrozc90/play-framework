package controllers.objects;

import actors.objects.ChangeLogDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.PurchaseOrder;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PurchaseOrderDto implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "number", required = true)
    private String number;

    @JsonProperty(value = "hash", required = true)
    private String hash;

    @JsonProperty(value = "status", required = true)
    private PurchaseOrder.Status status;

    @JsonProperty(value = "changelog", required = true)
    private ChangeLogDto changelog;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "order", required = true)
    private OrderDto order;

}
