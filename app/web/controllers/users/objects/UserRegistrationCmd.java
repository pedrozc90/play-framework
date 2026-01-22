package web.controllers.users.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationCmd {

    @NotNull
    @Size(min = 1, max = 255)
    @JsonProperty(value = "email", required = true)
    private final String email;

    @NotNull
    @Size(min = 6, max = 32)
    @JsonProperty(value = "password", required = true)
    private final String password;

    @JsonCreator
    public UserRegistrationCmd(@JsonProperty(value = "email", required = true) final String email,
                               @JsonProperty(value = "password", required = true) final String password) {
        this.email = email;
        this.password = password;
    }

}
