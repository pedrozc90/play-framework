package web.mappers;

import core.utils.DateUtils;
import core.utils.UuidUtils;
import domain.tasks.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import web.dtos.TaskDto;

@Mapper(uses = { DateUtils.class, UuidUtils.class })
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDto toDto(final Task entity);

    @Mappings({
        @Mapping(target = "job", ignore = true)
    })
    Task toEntity(@MappingTarget final Task entity, final TaskDto dto);

    default Task toEntity(final TaskDto dto) {
        return toEntity(new Task(), dto);
    }

}
