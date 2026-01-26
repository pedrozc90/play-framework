package web.mappers;

import core.utils.DateUtils;
import domain.files.FileStorage;
import domain.jobs.Job;
import web.dtos.FileStorageDto;
import web.dtos.JobDto;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.util.UUID;

@Singleton
public class JobMapper implements EntityMapper<Job, JobDto> {

    @Inject
    private FileStorageMapper fileStorageMapper;

    @Override
    public JobDto toDto(final Job entity) {
        if (entity == null) return null;
        final UUID uuid = UUID.fromString(entity.getUuid());
        final Instant insertedAt = DateUtils.toInstant(entity.getInsertedAt());
        final Instant updatedAt = DateUtils.toInstant(entity.getUpdatedAt());
        final FileStorageDto file = fileStorageMapper.toDto(entity.getFile());
        return new JobDto(
            uuid,
            insertedAt,
            updatedAt,
            entity.getVersion(),
            entity.getId(),
            entity.getStatus(),
            file
        );
    }

    @Override
    public Job toEntity(final Job entity, final JobDto dto) {
        entity.setId(dto.getId());
        entity.setUuid(dto.getUuid().toString());
        entity.setInsertedAt(DateUtils.toTimestamp(dto.getInsertedAt()));
        entity.setUpdatedAt(DateUtils.toTimestamp(dto.getUpdatedAt()));
        entity.setVersion(dto.getVersion());
        entity.setStatus(dto.getStatus());

        final FileStorage file = fileStorageMapper.toEntity(dto.getFile());
        entity.setFile(file);

        return entity;
    }

    @Override
    public Job toEntity(final JobDto dto) {
        return toEntity(new Job(), dto);
    }

}
