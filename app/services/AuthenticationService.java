package services;

import controllers.auth.objects.LoginResponse;
import core.auth.UserContext;
import core.exceptions.AppException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class AuthenticationService {

    private final UserService userService;
    private final TokenService tokenService;

    @Inject
    public AuthenticationService(
        final UserService userService,
        final TokenService tokenService
    ) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public CompletionStage<UserContext> validate(final String token) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final TokenService.DecodedToken decoded = tokenService.decode(token);

                return new UserContext(
                    token,
                    decoded.getUserId(),
                    decoded.getEmail(),
                    decoded.getTenantId(),
                    decoded.getRoles(),
                    decoded.getPermissions()
                );
            } catch (AppException e) {
                throw e.toCompletionException();
            }
        });
    }

    public CompletableFuture<LoginResponse> authenticate(final String email, final String password) {
        return userService.get(email, password)
            .thenApply((user) -> {
                final TokenService.EncodedToken result = tokenService.encode(user.getId(), user.getEmail(), null);
                return new LoginResponse(result.getToken(), result.issuedAt, result.expiresAt);
            });
    }
}
