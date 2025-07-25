package actors.objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChangeLogDto {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "changes")
    private final List<ChangeLogEntryDto> changes;

    public ChangeLogDto(final List<ChangeLogEntryDto> changes) {
        this.changes = (changes != null) ? changes : new ArrayList<>();
    }

    public ChangeLogDto() {
        this(new ArrayList<>());
    }

}
