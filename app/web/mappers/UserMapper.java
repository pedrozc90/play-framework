package web.mappers;

import core.utils.DateUtils;
import core.utils.UuidUtils;
import domain.users.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import web.dtos.UserDto;

@Mapper(uses = { DateUtils.class, UuidUtils.class })
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(final User entity);

    @Mappings({
        @Mapping(target = "password", ignore = true),
        @Mapping(target = "active", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    })
    User toEntity(@MappingTarget final User entity, final UserDto dto);

    default User toEntity(final UserDto dto) {
        return toEntity(new User(), dto);
    }

}
