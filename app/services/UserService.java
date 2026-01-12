package services;

import core.exceptions.AppException;
import core.utils.HashUtils;
import models.users.User;
import repositories.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserService {

    private final UserRepository repository;

    @Inject
    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    public User register(final String email, final String password) {
        return repository.wrap((em) -> {
            final User existent = repository.get(em, email);
            if (existent != null) {
                throw AppException.of(409, "User already exists").toCompletionException();
            }

            final String hashed = HashUtils.md5(password);
            final User user = new User();
            user.setEmail(email);
            user.setPassword(hashed);
            return repository.persist(em, user);
        });
    }

}
