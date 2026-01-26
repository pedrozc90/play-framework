package application.auth;

import application.auth.objects.JwtClaims;
import application.auth.objects.JwtDecoded;
import application.auth.objects.JwtEncoded;
import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import config.Configuration;
import core.exceptions.AppException;
import core.utils.http.HttpStatus;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class TokenService {

    private final long expiration;
    private final String issuer;
    private final String secret;

    private final JWTSigner signer;
    private final JWTSigner.Options opts;
    private final JWTVerifier verifier;

    @Inject
    public TokenService(final Configuration config) {
        issuer = config.getJwtIssuer();
        secret = config.getJwtSecret();

        final Duration duration = config.getJwtExpiration();
        expiration = (duration != null)
            ? duration.getSeconds()
            : 0;

        opts = new JWTSigner.Options();
        opts.setAlgorithm(Algorithm.HS256);
        opts.setIssuedAt(true);

        if (expiration > 0) {
            opts.setExpirySeconds((int) expiration);
        }

        signer = new JWTSigner(secret);

        verifier = new JWTVerifier(secret, null, issuer);
    }

    /**
     * Generate JWT token for a user
     */
    public JwtEncoded encode(final JwtClaims claims) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("sub", claims.getEmail());
        map.put("iss", issuer);
        map.put("userId", claims.getUserId());
        map.put("roles", claims.getRoles());
        map.put("permissions", claims.getPermissions());

        final Instant issuedAt = Instant.now();
        final Instant expiresAt = (expiration > 0)
            ? issuedAt.plusSeconds(expiration)
            : null;

        final String token = signer.sign(map, opts);

        return new JwtEncoded(token, issuedAt, expiresAt);
    }

    /**
     * Decode and verify JWT token
     */
    public JwtDecoded decode(final String token) throws AppException {
        try {
            final Map<String, Object> decoded = verifier.verify(token);

            final JwtClaims claims = new JwtClaims(
                asString(decoded.get("sub")),
                asLong(decoded.get("userId")),
                asSet(decoded.get("roles")),
                asSet(decoded.get("permissions"))
            );

            return new JwtDecoded(
                token,
                null, // issuedAt already validated by verifier
                null, // expiresAt already validated by verifier
                claims
            );
        } catch (JWTVerifyException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Invalid token");
        } catch (NoSuchAlgorithmException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Invalid algorithm");
        } catch (SignatureException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Invalid signature");
        } catch (InvalidKeyException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Invalid key");
        } catch (IOException e) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, e, "Token verification failed");
        }
    }

    private static String asString(final Object value) {
        if (value == null) return null;
        return (String) value;
    }

    private static Long asLong(final Object value) {
        if (value == null) return null;
        return ((Number) value).longValue();
    }

    private static Set<String> asSet(final Object value) {
        if (value == null) {
            return new HashSet<>();
        }
        if (value instanceof Iterable) {
            Set<String> set = new HashSet<>();
            for (Object o : (Iterable<?>) value) {
                if (o != null) {
                    set.add(o.toString());
                }
            }
            return set;
        }
        return new HashSet<>();
    }

}
