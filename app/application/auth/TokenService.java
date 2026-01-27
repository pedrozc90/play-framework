package application.auth;

import application.auth.objects.JwtClaims;
import application.auth.objects.JwtDecoded;
import application.auth.objects.JwtEncoded;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import config.Configuration;
import core.exceptions.AppException;
import core.utils.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class TokenService {

    private static final String USER_ID = "user_id";
    private static final String ROLES = "roles";
    private static final String PERMISSIONS = "permissions";

    private final long expiration;
    private final String issuer;
    private final String secret;

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    @Inject
    public TokenService(final Configuration config) {
        try {
            issuer = config.getJwtIssuer();
            secret = config.getJwtSecret();

            final Duration duration = config.getJwtExpiration();
            expiration = (duration != null)
                ? duration.getSeconds()
                : 0;

            algorithm = Algorithm.HMAC256(secret);

            verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to initialize jet algorithm", e);
        }
    }

    /**
     * Generate JWT token for a user
     */
    public JwtEncoded encode(final JwtClaims claims) {
        final Instant issuedAt = Instant.now();
        final Instant expiresAt = (expiration > 0)
            ? issuedAt.plusSeconds(expiration)
            : null;

        final JWTCreator.Builder builder = JWT.create()
            .withIssuer(issuer)
            .withSubject(claims.getEmail())
            .withClaim(USER_ID, claims.getUserId())
            .withArrayClaim(ROLES, toArray(claims.getRoles()))
            .withArrayClaim(PERMISSIONS, toArray(claims.getPermissions()))
            .withIssuedAt(Date.from(issuedAt));

        if (expiresAt != null) {
            builder.withExpiresAt(Date.from(expiresAt));
        }

        final String token = builder.sign(algorithm);

        return new JwtEncoded(token, issuedAt, expiresAt);
    }

    /**
     * Decode and verify JWT token
     */
    public JwtDecoded decode(final String token) throws AppException {
        try {
            final DecodedJWT decoded = verifier.verify(token);

            final JwtClaims claims = new JwtClaims(
                decoded.getSubject(),
                asLong(decoded.getClaim(USER_ID)),
                asSet(decoded.getClaim(ROLES)),
                asSet(decoded.getClaim(PERMISSIONS))
            );

            final Instant issuedAt = (decoded.getIssuedAt() != null) ? decoded.getIssuedAt().toInstant() : null;
            final Instant expiresAt = (decoded.getExpiresAt() != null) ? decoded.getExpiresAt().toInstant() : null;

            return new JwtDecoded(token, issuedAt, expiresAt, claims);
        } catch (AlgorithmMismatchException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Invalid algorithm");
        } catch (SignatureVerificationException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Invalid signature");
        } catch (TokenExpiredException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Token is expired");
        } catch (InvalidClaimException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Invalid claims");
        } catch (JWTVerificationException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Token verification failed");
        }
    }

    private static String asString(final Claim claim) {
        return (claim == null || claim.isNull())
            ? null
            : claim.asString();
    }

    private static Integer asInteger(final Claim claim) {
        return (claim == null || claim.isNull())
            ? null
            : claim.asInt();
    }

    private static Long asLong(final Claim claim) {
        return (claim == null || claim.isNull())
            ? null
            : claim.asLong();
    }

    private static Set<String> asSet(final Claim claim) {
        return claim == null || claim.isNull()
            ? new HashSet<>()
            : new HashSet<>(claim.asList(String.class));
    }

    private static String[] toArray(final Set<String> value) {
        return (value != null) ? value.toArray(new String[0]) : null;
    }

}
