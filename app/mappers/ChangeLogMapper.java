package mappers;

import actors.objects.ChangeLog;
import actors.objects.ChangeLogDto;
import actors.objects.ChangeLogEntry;
import actors.objects.ChangeLogEntryDto;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.util.List;
import java.util.stream.Collectors;

public class ChangeLogMapper {

    private static ChangeLogMapper instance;

    public static ChangeLogMapper getInstance() {
        if (instance == null) {
            instance = new ChangeLogMapper();
        }
        return instance;
    }

    public ChangeLogDto toDto(final String content) {
        if (content == null) return null;
        final JsonNode json = Json.parse(content);
        final ChangeLog changelog = Json.fromJson(json, ChangeLog.class);
        return toDto(changelog);
    }

    public ChangeLogDto toDto(final ChangeLog obj) {
        if (obj == null) return null;
        final List<ChangeLogEntryDto> changes = obj.getChanges().stream().map(this::toDto).collect(Collectors.toList());
        return new ChangeLogDto(changes);
    }

    public ChangeLogEntryDto toDto(final ChangeLogEntry entry) {
        if (entry == null) return null;
        return new ChangeLogEntryDto(entry.getEntityRef(), entry.toText());
    }

}
