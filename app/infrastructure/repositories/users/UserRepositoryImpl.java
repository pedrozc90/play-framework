package infrastructure.repositories.users;


import core.objects.Page;
import domain.users.User;
import infrastructure.repositories.DatabaseExecutionContext;
import infrastructure.repositories.JpaRepositoryImpl;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Singleton
public class UserRepositoryImpl extends JpaRepositoryImpl<User, Long> implements UserRepository {

    @Inject
    public UserRepositoryImpl(final JPAApi jpa, final DatabaseExecutionContext context) {
        super(jpa, context, User.class);
    }

    @Override
    public User get(final EntityManager em, final String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User get(final EntityManager em, final String email, final String password) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Stream<User> fetch(final EntityManager em) {
        return em.createQuery("SELECT u FROM User u", User.class)
            .getResultList()
            .stream();
    }

    public Page<User> fetch(final EntityManager em, final int page, final int rows, final String q, final Boolean active) {
        String where = "WHERE 1 = 1";
        final Map<String, Object> params = new HashMap<>();

        if (q != null && !q.isEmpty()) {
            where += " AND (u.email LIKE :q OR u.name LIKE :q)";
            params.put("q", "%" + q + "%");
        }

        if (active == Boolean.TRUE) {
            where += " AND u.active";
        } else if (active == Boolean.FALSE) {
            where += " AND NOT u.active";
        }

        final TypedQuery<User> listQuery = em.createQuery("SELECT u FROM User u " + where + " ORDER BY u.email", User.class);
        final TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(u) FROM User u " + where, Long.class);

        params.forEach((k, v) -> {
            listQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        final Long total = countQuery.getSingleResult();
        final List<User> list = listQuery.setFirstResult((page - 1) * rows)
            .setMaxResults(rows)
            .getResultList();

        return new Page<>(page, rows, total, list);
    }

    @Override
    public CompletableFuture<User> get(final String email) {
        return CompletableFuture.supplyAsync(() -> wrap((em) -> get(em, email)), context);
    }

    @Override
    public CompletableFuture<User> get(final String email, final String password) {
        return CompletableFuture.supplyAsync(() -> wrap((em) -> get(em, email, password)), context);
    }

    @Override
    public CompletableFuture<Stream<User>> list() {
        return CompletableFuture.supplyAsync(() -> wrap((em) -> fetch(em)), context);
    }

}
