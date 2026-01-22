package core.objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.utils.validation.Violation;
import lombok.Data;

import java.util.List;

@Data
public class ResultMessage {

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "message")
    private final String message;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "error")
    private final String error;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "stack")
    private final String stack;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "violations")
    private final List<Violation> violations;

    public ResultMessage(final String message, final String error, final String stack, final List<Violation> violations) {
        this.message = message;
        this.error = error;
        this.stack = stack;
        this.violations = violations;
    }

    public ResultMessage(final String message, final List<Violation> violations) {
        this(message, null, null, violations);
    }

    public ResultMessage(final String message) {
        this(message, null, null, null);
    }

}
