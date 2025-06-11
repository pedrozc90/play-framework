package controllers.objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.PurchaseOrder;

import java.io.Serializable;

public class PurchaseOrderDto implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "number", required = true)
    private String number;

    @JsonProperty(value = "hash", required = true)
    private String hash;

    @JsonProperty(value = "status", required = true)
    private PurchaseOrder.Status status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "order", required = true)
    private OrderDto order;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(final String hash) {
        this.hash = hash;
    }

    public PurchaseOrder.Status getStatus() {
        return status;
    }

    public void setStatus(final PurchaseOrder.Status status) {
        this.status = status;
    }

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(final OrderDto order) {
        this.order = order;
    }

}
