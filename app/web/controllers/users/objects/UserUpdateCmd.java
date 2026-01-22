package web.controllers.users.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserUpdateCmd extends UserRegistrationCmd {

    @JsonProperty(value = "active")
    private final boolean active;

    @JsonCreator
    public UserUpdateCmd(@JsonProperty(value = "email", required = true) final String email,
                         @JsonProperty(value = "password", required = true) final String password,
                         @JsonProperty(value = "active") final boolean active) {
        super(email, password);
        this.active = active;
    }

}
