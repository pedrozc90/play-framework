package services;

public interface EntityService<T> {

    void persist(final T entity);

    T save(final T entity);

    void remove(final T entity);

}
