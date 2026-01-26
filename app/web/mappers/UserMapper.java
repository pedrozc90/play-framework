package web.mappers;

import core.utils.DateUtils;
import domain.users.User;
import web.dtos.UserDto;

import javax.inject.Singleton;
import java.time.Instant;
import java.util.UUID;

@Singleton
public class UserMapper implements EntityMapper<User, UserDto> {

    @Override
    public UserDto toDto(final User entity) {
        if (entity == null) return null;
        final UUID uuid = UUID.fromString(entity.getUuid());
        final Instant insertedAt = DateUtils.toInstant(entity.getInsertedAt());
        final Instant updatedAt = DateUtils.toInstant(entity.getUpdatedAt());
        return new UserDto(
            uuid,
            insertedAt,
            updatedAt,
            uuid.version(),
            entity.getId(),
            entity.getEmail(),
            entity.isActive()
        );
    }

    @Override
    public User toEntity(final User entity, final UserDto dto) {
        entity.setId(dto.getId());
        entity.setUuid(dto.getUuid().toString());
        entity.setInsertedAt(DateUtils.toTimestamp(dto.getInsertedAt()));
        entity.setUpdatedAt(DateUtils.toTimestamp(dto.getUpdatedAt()));
        entity.setVersion(dto.getVersion());
        entity.setEmail(dto.getEmail());
        if (dto.getActive() != null) {
            entity.setActive(dto.getActive());
        }
        return entity;
    }

    @Override
    public User toEntity(final UserDto dto) {
        return toEntity(new User(), dto);
    }

}
