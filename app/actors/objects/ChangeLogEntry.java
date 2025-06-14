package actors.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeLogEntry {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum Type {
        ADDED,
        CHANGED,
        REMOVED,
    }

    @JsonProperty(value = "type")
    private final Type type;

    @JsonProperty(value = "field")
    private final String field;

    @JsonProperty(value = "old_value")
    private final String oldValue;

    @JsonProperty(value = "new_value")
    private final String newValue;

    @JsonProperty(value = "reference")
    private final String entityRef; // e.g., EAN or Order Number

    @JsonCreator
    public ChangeLogEntry(@JsonProperty(value = "type") final Type type,
                          @JsonProperty(value = "field") final String field,
                          @JsonProperty(value = "old_value") final String oldValue,
                          @JsonProperty(value = "new_value") final String newValue,
                          @JsonProperty(value = "reference") final String entityRef) {
        this.type = type;
        this.field = field;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.entityRef = entityRef;
    }

    public Type getType() {
        return type;
    }

    public String getField() {
        return field;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getEntityRef() {
        return entityRef;
    }

    public String toText() {
        if (type == null) return null;
        switch (type) {
            case ADDED: return String.format("added with %s = '%s'", field, newValue);
            case CHANGED: return String.format("changed %s from '%s' to '%s'", field, oldValue, newValue);
            case REMOVED: return String.format("removed %s = '%s'", field, oldValue);
            default: return null;
        }
    }

    public static ChangeLogEntry of(final Type type, final String field, final String oldValue, final String newValue, final String entityRef) {
        return new ChangeLogEntry(type, field, oldValue, newValue, entityRef);
    }

    public static ChangeLogEntry added(final String field, final String oldValue, final String newValue, final String entityRef) {
        return of(Type.ADDED, field, oldValue, newValue, entityRef);
    }

    public static ChangeLogEntry changed(final String field, final String oldValue, final String newValue, final String entityRef) {
        return of(Type.CHANGED, field, oldValue, newValue, entityRef);
    }

    public static ChangeLogEntry changed(final String field, final Integer oldValue, final Integer newValue, final String entityRef) {
        return changed(field, Integer.toString(oldValue), Integer.toString(newValue), entityRef);
    }

    public static ChangeLogEntry changed(final String field, final Boolean oldValue, final Boolean newValue, final String entityRef) {
        return changed(field, Boolean.toString(oldValue), Boolean.toString(newValue), entityRef);
    }

    public static ChangeLogEntry removed(final String field, final String oldValue, final String newValue, final String entityRef) {
        return of(Type.REMOVED, field, oldValue, newValue, entityRef);
    }

}
