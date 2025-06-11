package controllers.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(final String ean) {
        this.ean = ean;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(final String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

}
