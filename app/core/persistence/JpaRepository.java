package core.persistence;

import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;

public abstract class JpaRepository<T, ID> {

    protected final Class<T> clazz;
    protected final Logger.ALogger logger;

    public JpaRepository(final Class<T> clazz) {
        this.clazz = clazz;
        this.logger = Logger.of(getClass());
    }

    public EntityManager em() {
        return JPA.em();
    }

    public T findById(final ID id) {
        return em().find(clazz, id);
    }

    public T persist(final T entity) {
        em().persist(entity);
        return entity;
    }

    public T merge(final T entity) {
        return em().merge(entity);
    }

    public void remove(final T entity) {
        em().remove(entity);
    }

    public void flush() {
        em().flush();
    }

}
