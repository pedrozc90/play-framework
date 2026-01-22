package application.auth.objects;

import lombok.Data;

import java.time.Instant;

@Data
public class JwtEncoded {

    private final String token;
    private final Instant issuedAt;
    private final Instant expiresAt;

}
