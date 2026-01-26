package application.auth;

import application.auth.objects.JwtClaims;
import application.auth.objects.JwtDecoded;
import application.auth.objects.JwtEncoded;
import application.users.UserService;
import config.Configuration;
import core.exceptions.AppException;
import domain.users.User;
import play.mvc.Http;
import web.controllers.auth.objects.LoginResponse;
import web.security.objects.UserContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class AuthenticationService {

    public static final String COOKIE_NAME = "AccessToken";

    private final Configuration config;
    private final UserService userService;
    private final TokenService tokenService;

    @Inject
    public AuthenticationService(
        final Configuration config,
        final UserService userService,
        final TokenService tokenService
    ) {
        this.config = config;
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

    /* --- Cookies ---*/
    public Http.Cookie captureCookie(final Http.Request req) {
        return req.cookie(COOKIE_NAME);
    }

    public Http.Cookie createCookie(final String accessToken) {
        final Http.CookieBuilder builder = Http.Cookie.builder(COOKIE_NAME, accessToken)
            .withPath("/")
            .withHttpOnly(true);

        final String domain = config.getCookiesDomain();
        if (domain != null) {
            builder.withDomain(domain);
        }

        boolean isSecure = config.getCookiesSecure();
        builder.withSecure(isSecure);

        final Duration maxAge = config.getCookiesMaxAge();
        if (maxAge != null && maxAge.getSeconds() > 0) {
            builder.withMaxAge((int) maxAge.getSeconds());
        }

        return builder.build();
    }

}
