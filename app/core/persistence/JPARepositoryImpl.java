package core.persistence;

import play.Logger;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class JPARepositoryImpl<T, ID> implements JPARepository<T> {

    protected final Class<T> clazz;
    protected final JPAApi jpa;
    protected final DatabaseExecutionContext context;
    protected final Logger.ALogger logger;

    public JPARepositoryImpl(final Class<T> clazz, final JPAApi jpa, final DatabaseExecutionContext context) {
        this.clazz = clazz;
        this.jpa = jpa;
        this.context = context;
        this.logger = Logger.of(getClass());
    }

    @Override
    public <R> R wrap(final Function<EntityManager, R> function) {
        return jpa.withTransaction(function);
    }

    public T findById(final EntityManager em, final ID id) {
        return em.find(clazz, id);
    }

    @Override
    public T persist(final EntityManager em, final T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public CompletionStage<T> persist(final T entity) {
        return CompletableFuture.supplyAsync(() -> wrap((em) -> persist(em, entity)), context);
    }

    @Override
    public T merge(final EntityManager em, final T entity) {
        return em.merge(entity);
    }

    @Override
    public CompletionStage<T> merge(final T entity) {
        return CompletableFuture.supplyAsync(() -> wrap((em) -> merge(em, entity)), context);
    }

    @Override
    public T remove(final EntityManager em, final T entity) {
        em.remove(entity);
        return entity;
    }

    @Override
    public CompletionStage<T> remove(final T entity) {
        return CompletableFuture.supplyAsync(() -> wrap((em) -> remove(em, entity)), context);
    }

    @Override
    public void flush(final EntityManager em) {
        em.flush();
    }

}
