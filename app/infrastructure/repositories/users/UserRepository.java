package infrastructure.repositories.users;

import com.google.inject.ImplementedBy;
import core.objects.Page;
import domain.users.User;
import infrastructure.repositories.JpaRepository;

import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@ImplementedBy(UserRepositoryImpl.class)
public interface UserRepository extends JpaRepository<User> {

    User findById(final EntityManager em, final Long id);

    User get(final EntityManager em, final String email, final String hashed);

    User get(final EntityManager em, final String email);

    CompletableFuture<User> get(final String email);

    CompletableFuture<User> get(final String email, final String password);

    CompletableFuture<Stream<User>> list();

    Page<User> fetch(final EntityManager em, final int page, final int rows, final String q, final Boolean active);
}
