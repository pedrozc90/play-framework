package actors.objects;

import java.util.ArrayList;
import java.util.List;

public class Resolved<T> {

    private final T entity;

    private final List<ChangeLogEntry> changes;

    public Resolved(final T entity, final List<ChangeLogEntry> changes) {
        this.entity = entity;
        this.changes = changes;
    }

    public Resolved(final T entity) {
        this(entity, new ArrayList<>());
    }

    public T getEntity() {
        return entity;
    }

    public List<ChangeLogEntry> getChanges() {
        return changes;
    }

}
