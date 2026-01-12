package core.mappers;

import controllers.files.objects.FileStorageDto;
import models.files.FileStorage;

import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Singleton
public class FileStorageMapper implements EntityMapper<FileStorage, FileStorageDto> {

    @Override
    public FileStorageDto toDto(final FileStorage entity) {
        if (entity == null) return null;
        final UUID uuid = UUID.fromString(entity.getUuid());
        final Instant insertedAt = entity.getInsertedAt().toInstant();
        final Instant updatedAt = entity.getUpdatedAt().toInstant();
        return new FileStorageDto(
            uuid,
            insertedAt,
            updatedAt,
            entity.getVersion(),
            entity.getId(),
            entity.getHash(),
            entity.getFilename(),
            entity.getContentType(),
            entity.getCharset(),
            entity.getLength()
        );
    }

    @Override
    public FileStorage toModel(final FileStorage entity, final FileStorageDto dto) {
        entity.setId(dto.getId());
        entity.setUuid(dto.getUuid().toString());
        entity.setInsertedAt(new Timestamp(dto.getInsertedAt().toEpochMilli()));
        entity.setUpdatedAt(new Timestamp(dto.getUpdatedAt().toEpochMilli()));
        entity.setVersion(dto.getVersion());
        entity.setHash(dto.getHash());
        entity.setFilename(dto.getFilename());
        entity.setContentType(dto.getContentType());
        entity.setCharset(dto.getCharset());
        entity.setLength(dto.getLength());
        return entity;
    }

    @Override
    public FileStorage toModel(final FileStorageDto dto) {
        return toModel(new FileStorage(), dto);
    }

}
