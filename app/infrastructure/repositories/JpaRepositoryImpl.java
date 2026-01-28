package infrastructure.repositories;

import play.Logger;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class JpaRepositoryImpl<T, ID> implements JpaRepository<T> {

    protected final Logger.ALogger logger;
    protected final JPAApi jpa;
    protected final DatabaseExecutionContext context;
    protected final Class<T> clazz;

    public JpaRepositoryImpl(final JPAApi jpa, final DatabaseExecutionContext context, final Class<T> clazz) {
        this.logger = Logger.of(getClass());
        this.jpa = jpa;
        this.context = context;
        this.clazz = clazz;
    }

    @Override
    public <R> R wrap(final Function<EntityManager, R> function) {
        return jpa.withTransaction(function);
    }

    public T findById(final EntityManager em, final ID id) {
        return em.find(clazz, id);
    }

    public CompletionStage<T> findById(final ID id) {
        return CompletableFuture.supplyAsync(() -> wrap((em) -> findById(em, id)), context);
    }

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
