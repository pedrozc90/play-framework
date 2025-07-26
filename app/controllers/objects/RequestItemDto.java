package controllers.objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class RequestItemDto {

    @JsonProperty(value = "ean", required = true)
    private String ean;

    @JsonProperty(value = "quantity", required = true)
    private Integer quantity = 0;

    @JsonProperty(value = "description", required = true)
    private String description;

    @JsonProperty(value = "label_type", required = true)
    private String labelType;

    @JsonIgnore
    private Map<String, Object> metadata = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> any() {
        return metadata;
    }

    @JsonAnySetter
    public void set(final String key, final Object value) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        metadata.put(key, value);
    }

}
