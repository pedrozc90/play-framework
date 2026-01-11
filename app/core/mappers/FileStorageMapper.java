package core.mappers;

import controllers.files.objects.FileStorageDto;
import models.files.FileStorage;

import java.sql.Timestamp;
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
        final FileStorageDto dto = new FileStorageDto();
        dto.setId(entity.getId());
        dto.setUuid(UUID.fromString(entity.getUuid()));
        dto.setInsertedAt(entity.getInsertedAt().toInstant());
        dto.setUpdatedAt(entity.getUpdatedAt().toInstant());
        dto.setVersion(entity.getVersion());
        dto.setHash(entity.getHash());
        dto.setFilename(entity.getFilename());
        dto.setContentType(entity.getContentType());
        dto.setCharset(entity.getCharset());
        dto.setLength(entity.getLength());
        return dto;
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
