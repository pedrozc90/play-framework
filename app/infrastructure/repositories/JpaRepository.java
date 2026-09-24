package infrastructure.repositories;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import core.objects.Page;
import domain.users.User;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class JpaRepository<T, E extends EntityPathBase<T>, ID> {

    protected final Logger.ALogger logger;
    protected final Class<T> clazz;
    protected final E entity;

    public JpaRepository(final Class<T> clazz, final E entity) {
        this.logger = Logger.of(getClass());
        this.clazz = clazz;
        this.entity = entity;
    }

    public EntityManager em() {
        return JPA.em();
    }

    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em());
    }

    public JPAQuery<T> createQuery() {
        return queryFactory().selectFrom(entity);
    }

    public T findById(final EntityManager em, final ID id) {
        return em.find(clazz, id);
    }

    public T findById(final ID id) {
        return findById(em(), id);
    }

    public T persist(final T entity) {
        em().persist(entity);
        return entity;
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

    public <R> Page<R> fetch(final JPAQuery<R> query, final int page, final int rows) {
        final long total = query.clone().fetchCount();

        final List<R> list = query
            .offset((long) (page - 1) * rows)
            .limit(rows)
            .fetch();

        return new Page<>(page, rows, total, list);
    }

}
