package core.utils.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Violation {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "field")
    private final String field;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "message")
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "value")
    private final String value;

}
