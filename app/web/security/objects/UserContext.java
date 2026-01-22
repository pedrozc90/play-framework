package web.security.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserContext implements Serializable {

    @JsonProperty(value = "token")
    private final String token;

    @JsonProperty(value = "user_id")
    private final Long userId;

    @JsonProperty(value = "email")
    private final String email;

    @JsonProperty(value = "roles")
    private final Set<String> roles;

    @JsonProperty(value = "permissions")
    private final Set<String> permissions;

    public boolean hasRole(final String value) {
        return roles != null && roles.contains(value);
    }

    public boolean hasPermission(final String value) {
        return permissions != null && permissions.contains(value);
    }

    @JsonIgnore
    public boolean isAdmin() {
        return hasRole("admin");
    }

}
