package application.auth;

import application.auth.objects.JwtClaims;
import application.auth.objects.JwtDecoded;
import application.auth.objects.JwtEncoded;
import application.users.UserService;
import core.exceptions.AppException;
import domain.users.User;
import web.controllers.auth.objects.LoginResponse;
import web.security.objects.UserContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

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

    public UserContext validate(final String token) throws AppException {
        final JwtDecoded decoded = tokenService.decode(token);
        return new UserContext(
            token,
            decoded.getClaims().getUserId(),
            decoded.getClaims().getEmail(),
            decoded.getClaims().getRoles(),
            decoded.getClaims().getPermissions()
        );
    }

    public LoginResponse authenticate(final String email, final String password) throws AppException {
        final User user = userService.get(email, password);
        final Set<String> roles = new HashSet<>();
        final Set<String> permissions = new HashSet<>();
        final JwtClaims claims = new JwtClaims(user.getEmail(), user.getId(), roles, permissions);
        final JwtEncoded result = tokenService.encode(claims);
        return new LoginResponse(result.getToken(), result.getIssuedAt(), result.getExpiresAt());
    }

}
