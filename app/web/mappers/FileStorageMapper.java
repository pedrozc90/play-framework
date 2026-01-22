package web.mappers;


import core.utils.DateUtils;
import domain.files.FileStorage;
import web.dtos.FileStorageDto;

import java.time.Instant;
import java.util.UUID;

public class FileStorageMapper implements EntityMapper<FileStorage, FileStorageDto> {

    private static FileStorageMapper instance;

    public static FileStorageMapper getInstance() {
        if (instance == null) {
            instance = new FileStorageMapper();
        }
        return instance;
    }

    @Override
    public FileStorageDto toDto(final FileStorage entity) {
        if (entity == null) return null;
        final UUID uuid = UUID.fromString(entity.getUuid());
        final Instant insertedAt = DateUtils.toInstant(entity.getInsertedAt());
        final Instant updatedAt = DateUtils.toInstant(entity.getUpdatedAt());
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
    public FileStorage toEntity(final FileStorage entity, final FileStorageDto dto) {
        entity.setId(dto.getId());
        entity.setUuid(dto.getUuid().toString());
        entity.setInsertedAt(DateUtils.toTimestamp(dto.getInsertedAt()));
        entity.setUpdatedAt(DateUtils.toTimestamp(dto.getUpdatedAt()));
        entity.setVersion(dto.getVersion());
        entity.setHash(dto.getHash());
        entity.setFilename(dto.getFilename());
        entity.setContentType(dto.getContentType());
        entity.setCharset(dto.getCharset());
        entity.setLength(dto.getLength());
        return entity;
    }

    @Override
    public FileStorage toEntity(final FileStorageDto dto) {
        return toEntity(new FileStorage(), dto);
    }

}
