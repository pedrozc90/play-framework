package services;

import core.exceptions.AppException;
import core.utils.HashUtils;
import core.utils.http.HttpStatus;
import models.users.User;
import repositories.UserRepository;
import scala.App;

import javax.inject.Singleton;

@Singleton
public class UserService {

    private static UserService instance;

    private final UserRepository repository = UserRepository.getInstance();

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

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

    public User get(final Long id) throws AppException {
        final User user = repository.findById(id);
        if (user == null) {
            throw AppException.of(HttpStatus.NOT_FOUND, "User (%d) not found", id);
        }
        return user;
    }

    public User get(final String email, final String password) {
        final String hashed = HashUtils.md5(password);
        return repository.get(email, hashed);
    }

}
