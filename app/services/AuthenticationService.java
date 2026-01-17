package services;

import controllers.auth.objects.LoginResponse;
import core.auth.UserContext;
import core.exceptions.AppException;
import models.users.User;
import repositories.FileStorageRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Singleton
public class AuthenticationService {

    private static AuthenticationService instance;

    private final UserService userService = UserService.getInstance();
    private final TokenService tokenService = TokenService.getInstance();

    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public UserContext validate(final String token) throws AppException {
        final TokenService.DecodedToken decoded = tokenService.decode(token);

        return new UserContext(
            token,
            decoded.getUserId(),
            decoded.getEmail(),
            decoded.getTenantId(),
            decoded.getRoles(),
            decoded.getPermissions()
        );
    }

    public LoginResponse authenticate(final String email, final String password) {
        final User user = userService.get(email, password);
        final TokenService.EncodedToken result = tokenService.encode(user.getId(), user.getEmail(), null);
        return new LoginResponse(result.getToken(), result.issuedAt, result.expiresAt);
    }

}
