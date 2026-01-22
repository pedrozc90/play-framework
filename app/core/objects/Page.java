package core.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class Page<T> {

    @JsonProperty(value = "page")
    private final int page;

    @JsonProperty(value = "rows")
    private final int rows;

    @JsonProperty(value = "total")
    private final long total;

    @JsonProperty(value = "list")
    private final List<T> list;

    public <E> Page<E> map(final Function<T, E> mapper) {
        final List<E> collect = list.stream()
            .map(mapper)
            .collect(Collectors.toList());
        return new Page<>(page, rows, total, collect);
    }

}
