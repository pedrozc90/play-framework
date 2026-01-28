package application.users;

import core.exceptions.AppException;
import core.objects.Page;
import core.utils.HashUtils;
import core.utils.http.HttpStatus;
import domain.users.User;
import infrastructure.repositories.users.UserRepository;
import web.controllers.users.objects.UserUpdateCmd;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class UserService {

    @Inject
    private UserRepository repository;

    // QUERY
    public User get(final EntityManager em, final Long id) throws AppException {
        final User user = repository.findById(em, id);
        if (user == null) {
            throw AppException.of(HttpStatus.NOT_FOUND, "User (%d) not found", id);
        }
        return user;
    }

    public CompletionStage<User> getAsync(final Long id) {
        return CompletableFuture.supplyAsync(() -> repository.wrap((em -> repository.findById(em, id))));
    }

    private User get(final EntityManager em, final String email, final String password) throws AppException {
        final String hashed = HashUtils.md5(password);
        final User user = repository.get(em, email, hashed);
        if (user == null) {
            throw AppException.of(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    public CompletionStage<User> get(final String email, final String password) {
        return CompletableFuture.supplyAsync(() -> repository.wrap((em) -> {
            try {
                return get(em, email, password);
            } catch (AppException e) {
                throw e.toCompletionException();
            }
        }));
    }

    // COMMANDS
    public User register(final EntityManager em, final String email, final String password) throws AppException {
        final User existent = repository.get(em, email);
        if (existent != null) {
            throw AppException.of(HttpStatus.CONFLICT, "User already exists");
        }

        final String hashed = HashUtils.md5(password);
        final User user = new User();
        user.setEmail(email);
        user.setPassword(hashed);
        return repository.persist(em, user);
    }

    public User update(final EntityManager em, final Long id, final UserUpdateCmd cmd) throws AppException {
        final User user = this.get(em, id);
        if (!Objects.equals(user.getEmail(), cmd.getEmail())) {
            user.setEmail(cmd.getEmail());
        }

        if (!Objects.equals(user.getPassword(), cmd.getPassword())) {
            user.setPassword(cmd.getPassword());
        }

        if (!Objects.equals(user.isActive(), cmd.isActive())) {
            user.setActive(cmd.isActive());
        }

        return repository.merge(em, user);
    }

    public void remove(final User user) {
        repository.remove(user);
    }

    public CompletableFuture<Page<User>> fetch(final int page, final int rows, final String q, final Boolean active) {
        return CompletableFuture.supplyAsync(() -> repository.wrap((em -> repository.fetch(em, page, rows, q, active))));
    }

}
