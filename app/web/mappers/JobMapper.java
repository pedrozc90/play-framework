package web.mappers;

import core.utils.DateUtils;
import core.utils.UuidUtils;
import domain.jobs.Job;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import web.dtos.JobDto;

@Mapper(uses = { DateUtils.class, UuidUtils.class, FileStorageMapper.class })
public interface JobMapper {

    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

    JobDto toDto(final Job entity);

    Job toEntity(@MappingTarget final Job entity, final JobDto dto);

    default Job toEntity(final JobDto dto) {
        return toEntity(new Job(), dto);
    }

}
