package web.mappers;

import core.utils.DateUtils;
import domain.tasks.Task;
import web.dtos.TaskDto;

import java.time.Instant;
import java.util.UUID;

public class TaskMapper implements EntityMapper<Task, TaskDto> {

    private static TaskMapper instance;

    public static TaskMapper getInstance() {
        if (instance == null) {
            instance = new TaskMapper();
        }
        return instance;
    }

    @Override
    public TaskDto toDto(final Task entity) {
        if (entity == null) return null;
        final UUID uuid = UUID.fromString(entity.getUuid());
        final Instant insertedAt = DateUtils.toInstant(entity.getInsertedAt());
        final Instant updatedAt = DateUtils.toInstant(entity.getUpdatedAt());
        return new TaskDto(
            uuid,
            insertedAt,
            updatedAt,
            uuid.version(),
            entity.getId(),
            entity.getType(),
            entity.getStatus(),
            entity.getStackTrace(),
            entity.getStartedAt().toInstant(),
            entity.getCompletedAt().toInstant()
        );
    }

    @Override
    public Task toEntity(final Task entity, final TaskDto dto) {
        entity.setId(dto.getId());
        entity.setUuid(dto.getUuid().toString());
        entity.setInsertedAt(DateUtils.toTimestamp(dto.getInsertedAt()));
        entity.setUpdatedAt(DateUtils.toTimestamp(dto.getUpdatedAt()));
        entity.setVersion(dto.getVersion());
        entity.setType(dto.getType());
        entity.setStatus(dto.getStatus());
        entity.setStackTrace(dto.getStackTrace());
        entity.setStartedAt(DateUtils.toTimestamp(dto.getStartedAt()));
        entity.setCompletedAt(DateUtils.toTimestamp(dto.getCompletedAt()));
        return entity;
    }

    @Override
    public Task toEntity(final TaskDto dto) {
        return toEntity(new Task(), dto);
    }

}
