package actors.objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ChangeLog {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "changes")
    private final List<ChangeLogEntry> changes = new ArrayList<>();

}
