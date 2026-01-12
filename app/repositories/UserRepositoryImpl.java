package repositories;

import core.persistence.DatabaseExecutionContext;
import core.persistence.JPARepositoryImpl;
import models.users.User;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class UserRepositoryImpl extends JPARepositoryImpl<User, Long> implements UserRepository {

    @Inject
    public UserRepositoryImpl(final JPAApi jpa, final DatabaseExecutionContext context) {
        super(User.class, jpa, context);
    }

    @Override
    public User get(final EntityManager em, final String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
            .setParameter("email", email)
            .getSingleResult();
    }

    public User get(final EntityManager em, final String email, final String password) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
            .setParameter("email", email)
            .setParameter("password", password)
            .getSingleResult();
    }

    public Stream<User> fetch(final EntityManager em) {
        return em.createQuery("SELECT u FROM User u", User.class)
            .getResultList()
            .stream();
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
