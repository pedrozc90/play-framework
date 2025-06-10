package utils.mappers;

public interface EntityMapper<T, R> {

    R toDto(final T entity);

    T toModel(final T entity, final R dto);

    T toModel(final R dto);

}
