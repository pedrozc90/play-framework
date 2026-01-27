package application.auth.objects;

import lombok.Data;

import java.time.Instant;

@Data
public class JwtDecoded {

    private final String token;
    private final Instant issuedAt;
    private final Instant expiresAt;
    private final JwtClaims claims;

}
