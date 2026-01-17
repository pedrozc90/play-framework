package core.mappers;

import controllers.users.objects.UserDto;
import models.users.User;

import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Singleton
public class UserMapper implements EntityMapper<User, UserDto> {

    private static UserMapper instance;

    public static UserMapper getInstance() {
        if (instance == null) {
            instance = new UserMapper();
        }
        return instance;
    }

    @Override
    public UserDto toDto(final User entity) {
        if (entity == null) return null;
        final UUID uuid = UUID.fromString(entity.getUuid());
        final Instant insertedAt = entity.getInsertedAt().toInstant();
        final Instant updatedAt = entity.getUpdatedAt().toInstant();
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
    public User toModel(final User entity, final UserDto dto) {
        entity.setId(dto.getId());
        entity.setUuid(dto.getUuid().toString());
        entity.setInsertedAt(new Timestamp(dto.getInsertedAt().toEpochMilli()));
        entity.setUpdatedAt(new Timestamp(dto.getUpdatedAt().toEpochMilli()));
        entity.setVersion(dto.getVersion());
        entity.setEmail(dto.getEmail());
        if (dto.getActive() != null) {
            entity.setActive(dto.getActive());
        }
        return entity;
    }

    @Override
    public User toModel(final UserDto dto) {
        return toModel(new User(), dto);
    }

}
