package controllers.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItemDto {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "quantity", required = true)
    private Integer quantity = 0;

    @JsonProperty(value = "label_type", required = true)
    private String labelType;

    @JsonProperty(value = "product", required = true)
    private ProductDto product;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(final String labelType) {
        this.labelType = labelType;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(final ProductDto product) {
        this.product = product;
    }

}
