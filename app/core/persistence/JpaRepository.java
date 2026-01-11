package core.persistence;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;

public abstract class JpaRepository<T, ID> {

    protected final Class<T> clazz;
    protected final JPAApi jpaApi;
    protected final Logger.ALogger logger;

    public JpaRepository(final Class<T> clazz, final JPAApi jpaApi) {
        this.clazz = clazz;
        this.jpaApi = jpaApi;
        this.logger = Logger.of(getClass());
    }

    public EntityManager em() {
        // return jpaApi.em("default"); // not working, it creates a new EntityManager on every call.
        return JPA.em();                // I was supposed to remove this, but it works... (-_-")
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
