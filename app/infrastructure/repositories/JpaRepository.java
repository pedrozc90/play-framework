package infrastructure.repositories;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;

public abstract class JpaRepository<T, ID> {

    protected final Logger.ALogger logger;
    protected final JPAApi jpa;
    protected final Class<T> clazz;

    public JpaRepository(final JPAApi jpa, final Class<T> clazz) {
        this.logger = Logger.of(getClass());
        this.jpa = jpa;
        this.clazz = clazz;
    }

    public EntityManager em() {
        // return jpaApi.em("default"); // not working, it creates a new EntityManager on every call.
        return JPA.em();                // I was supposed to remove this, but it works... (-_-")
    }

    public T findById(final EntityManager em, final ID id) {
        return em.find(clazz, id);
    }

    public T findById(final ID id) {
        return findById(em(), id);
    }

    public T persist(final EntityManager em, final T entity) {
        em.persist(entity);
        return entity;
    }

    public T persist(final T entity) {
        return persist(em(), entity);
    }

    public T merge(final T entity) {
        return em().merge(entity);
    }

    public void remove(final EntityManager em, final T entity) {
        em.remove(entity);
    }

    public void remove(final T entity) {
        remove(em(), entity);
    }

    public void flush(final EntityManager em) {
        em.flush();
    }

    public void flush() {
        flush(em());
    }

}
