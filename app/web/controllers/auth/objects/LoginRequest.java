package web.controllers.auth.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {

    @NotNull
    @Size(min = 1, max = 255)
    @JsonProperty(value = "email", required = true)
    private String email;

    @NotNull
    @Size(min = 6, max = 32)
    @JsonProperty(value = "password", required = true)
    private String password;

}
