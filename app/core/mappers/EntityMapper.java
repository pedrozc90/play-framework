package core.mappers;

public interface EntityMapper<Entity, DTO> {

    DTO toDto(final Entity entity);

    Entity toModel(final Entity entity, final DTO dto);

    Entity toModel(final DTO dto);

}
