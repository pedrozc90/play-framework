package application.auth.objects;

import lombok.Data;

import java.util.Set;

@Data
public class JwtClaims {

    private final String email;
    private final Long userId;
    private final Set<String> roles;
    private final Set<String> permissions;

}
