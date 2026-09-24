package web.mappers;

import core.utils.DateUtils;
import core.utils.UuidUtils;
import domain.files.FileStorage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import web.dtos.FileStorageDto;

@Mapper(uses = { DateUtils.class, UuidUtils.class })
public interface FileStorageMapper {

    FileStorageMapper INSTANCE = Mappers.getMapper(FileStorageMapper.class);

    FileStorageDto toDto(final FileStorage entity);

    @Mappings({
        @Mapping(target = "content", ignore = true),
        @Mapping(target = "extension", ignore = true)
    })
    FileStorage toEntity(@MappingTarget final FileStorage entity, final FileStorageDto dto);

    default FileStorage toEntity(final FileStorageDto dto) {
        return toEntity(new FileStorage(), dto);
    }

}
