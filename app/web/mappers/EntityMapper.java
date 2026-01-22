package web.mappers;

public interface EntityMapper<Entity, DTO> {

    DTO toDto(final Entity entity);

    Entity toEntity(final Entity entity, final DTO dto);

    Entity toEntity(final DTO dto);

}
