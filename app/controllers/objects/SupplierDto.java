package controllers.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SupplierDto implements Serializable {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "code", required = true)
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

}
