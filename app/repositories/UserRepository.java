package repositories;

import com.google.inject.ImplementedBy;
import core.persistence.JPARepository;
import models.users.User;

import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@ImplementedBy(UserRepositoryImpl.class)
public interface UserRepository extends JPARepository<User> {

    User get(final EntityManager em, final String email);

    CompletableFuture<User> get(final String email);

    CompletableFuture<User> get(final String email, final String password);

    CompletableFuture<Stream<User>> list();

}
