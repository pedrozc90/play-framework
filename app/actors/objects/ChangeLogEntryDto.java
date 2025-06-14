package actors.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeLogEntryDto {

    @JsonProperty(value = "field")
    private final String field;

    @JsonProperty(value = "change")
    private final String change;

    @JsonCreator
    public ChangeLogEntryDto(@JsonProperty(value = "field") final String field,
                             @JsonProperty(value = "change") final String change) {
        this.field = field;
        this.change = change;
    }

    public String getField() {
        return field;
    }

    public String getChange() {
        return change;
    }

}
