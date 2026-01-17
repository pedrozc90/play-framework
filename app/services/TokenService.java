package services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import core.config.Configuration;
import core.exceptions.AppException;
import core.utils.http.HttpStatus;
import lombok.Data;

import javax.inject.Singleton;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Singleton
public class TokenService {

    public static TokenService instance;

    public static final Configuration config = Configuration.getInstance();

    private final long expiration;
    private final String issuer;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public TokenService() {
        issuer = config.getJwtIssuer();

        final String secret = config.getJwtSecret();
        algorithm = Algorithm.HMAC256(secret);

        expiration = config.getJwtExpiration();

        verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .withIssuer(issuer)
            .build();
    }

    public static TokenService getInstance() {
        if (instance == null) {
            synchronized (TokenService.class) {
                if (instance == null) {
                    instance = new TokenService();
                }
            }
        }
        return instance;
    }

    /**
     * Generate JWT token for a user
     */
    public EncodedToken encode(final Long userId, final String email, final Long tenantId) {
        final Instant issuedAt = Instant.now();
        final Instant expiresAt = issuedAt.plus(expiration, ChronoUnit.MINUTES);

        final String token = JWT.create()
            .withIssuer(issuer)
            .withSubject(email)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withClaim("tenantId", tenantId)
            .withIssuedAt(issuedAt)
            .withExpiresAt(expiresAt)
            .sign(algorithm);

        return new EncodedToken(token, issuedAt, expiresAt);
    }

    /**
     * Decode and verify JWT token
     */
    public DecodedToken decode(final String token) throws AppException {
        try {
            final DecodedJWT decoded = verifier.verify(token);
            final Instant issuedAt = decoded.getIssuedAtAsInstant();
            final Instant expiresAt = decoded.getExpiresAtAsInstant();
            final String email = decoded.getSubject();

            final Long userId = Optional.ofNullable(decoded.getClaim("userId"))
                .map(Claim::asLong)
                .orElse(null);

            final Long tenantId = Optional.ofNullable(decoded.getClaim("tenantId"))
                .map(v -> v.asLong())
                .orElse(null);

            final Set<String> roles = Optional.ofNullable(decoded.getClaim("roles"))
                .map(v -> v.asList(String.class))
                .map(v -> new HashSet<>(v))
                .orElse(null);

            final Set<String> permissions = Optional.ofNullable(decoded.getClaim("permissions"))
                .map(v -> v.asList(String.class))
                .map(v -> new HashSet<>(v))
                .orElse(null);

            return new DecodedToken(token, issuedAt, expiresAt, userId, email, tenantId, roles, permissions);
        } catch (TokenExpiredException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, "Token expired");
        } catch (JWTVerificationException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    @Data
    public static class EncodedToken {

        private final String token;
        final Instant issuedAt;
        final Instant expiresAt;

    }

    @Data
    public static class DecodedToken {

        private final String token;
        private final Instant issuedAt;
        private final Instant expiresAt;
        private final Long userId;
        private final String email;
        private final Long tenantId;
        private final Set<String> roles;
        private final Set<String> permissions;

    }

}
