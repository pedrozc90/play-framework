package web.controllers.users.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserUpdateCmd {

    @NotNull
    @Size(min = 1, max = 255)
    @JsonProperty(value = "email", required = true)
    private final String email;

    // Optional on update: when omitted/null the user's existing password is left unchanged.
    @Size(min = 6, max = 32)
    @JsonProperty(value = "password")
    private final String password;

    @JsonProperty(value = "active")
    private final boolean active;

    @JsonCreator
    public UserUpdateCmd(
        @JsonProperty(value = "email", required = true) final String email,
        @JsonProperty(value = "password") final String password,
        @JsonProperty(value = "active") final boolean active
    ) {
        this.email = email;
        this.password = password;
        this.active = active;
    }

}
