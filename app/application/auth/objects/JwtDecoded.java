package application.auth.objects;

import lombok.Data;

@Data
public class JwtDecoded {

    private final String token;
    private final Long issuedAt;
    private final Long expiresAt;
    private final JwtClaims claims;

}
