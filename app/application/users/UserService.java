package application.users;

import core.exceptions.AppException;
import core.objects.Page;
import core.utils.HashUtils;
import core.utils.http.HttpStatus;
import domain.users.User;
import infrastructure.repositories.UserRepository;
import web.controllers.users.objects.UserUpdateCmd;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class UserService {

    @Inject
    private UserRepository repository;

    // QUERY
    public User get(final Long id) throws AppException {
        final User user = repository.findById(id);
        if (user == null) {
            throw AppException.of(HttpStatus.NOT_FOUND, "User (%d) not found", id);
        }
        return user;
    }

    public User get(final String email, final String password) throws AppException {
        final String hashed = HashUtils.md5(password);
        final User user = repository.get(email, hashed);
        if (user == null) {
            throw AppException.of(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    // COMMANDS
    public User register(final String email, final String password) throws AppException {
        final User existent = repository.get(email);
        if (existent != null) {
            throw AppException.of(HttpStatus.CONFLICT, "User already exists");
        }

        final String hashed = HashUtils.md5(password);
        final User user = new User();
        user.setEmail(email);
        user.setPassword(hashed);
        return repository.persist(user);
    }

    public User update(final Long id, final UserUpdateCmd cmd) throws AppException {
        final User user = this.get(id);
        if (!Objects.equals(user.getEmail(), cmd.getEmail())) {
            user.setEmail(cmd.getEmail());
        }

        if (!Objects.equals(user.getPassword(), cmd.getPassword())) {
            user.setPassword(cmd.getPassword());
        }

        if (!Objects.equals(user.isActive(), cmd.isActive())) {
            user.setActive(cmd.isActive());
        }

        return repository.merge(user);
    }

    public void remove(final User user) {
        repository.remove(user);
    }

    public Page<User> fetch(final int page, final int rows, final String q, final Boolean active) {
        return repository.fetch(page, rows, q, active);
    }

}
