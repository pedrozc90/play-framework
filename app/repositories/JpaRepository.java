package repositories;

import models.files.FileStorage;
import play.db.jpa.JPA;

import java.util.UUID;

public abstract class JpaRepository<T, ID> {

    private final Class<T> clazz;

    public JpaRepository(final Class<T> clazz) {
        this.clazz = clazz;
    }

    public T findById(final ID id) {
        return JPA.em().find(clazz, id);
    }

    public T persist(final T entity) {
        JPA.em().persist(entity);
        return entity;
    }

    public T merge(final T entity) {
        return JPA.em().merge(entity);
    }

    public void remove(final T entity) {
        JPA.em().remove(entity);
    }

    public void flush() {
        JPA.em().flush();
    }

}
