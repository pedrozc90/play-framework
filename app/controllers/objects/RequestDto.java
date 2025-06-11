package controllers.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Set;

public class RequestDto implements Serializable {

    @JsonProperty(value = "number", required = true)
    private String number;

    @JsonProperty(value = "supplier", required = true)
    private String supplier;

    @JsonProperty(value = "status", required = true)
    private String status;

    @JsonProperty(value = "items", required = true)
    private Set<RequestItemDto> items;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<RequestItemDto> getItems() {
        return items;
    }

    public void setItems(Set<RequestItemDto> items) {
        this.items = items;
    }

}
