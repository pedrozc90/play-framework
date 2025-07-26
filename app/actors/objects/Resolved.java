package actors.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Resolved<T> {

    private final T entity;

    private final List<ChangeLogEntry> changes;

    public Resolved(final T entity) {
        this(entity, new ArrayList<>());
    }

}
