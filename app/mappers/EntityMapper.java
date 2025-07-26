package mappers;

public interface EntityMapper<T, R> {

    R toDto(final T entity);

    T toEntity(final T entity, final R dto);

    T toEntity(final R dto);

}
