package infrastructure.repositories;

import javax.persistence.EntityManager;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface JpaRepository<T> {

    <R> R wrap(final Function<EntityManager, R> function);

    T persist(final EntityManager em, final T entity);

    CompletionStage<T> persist(final T entity);

    T merge(final EntityManager em, final T entity);

    CompletionStage<T> merge(final T entity);

    T remove(final EntityManager em, final T entity);

    CompletionStage<T> remove(final T entity);

    void flush(final EntityManager em);

}
