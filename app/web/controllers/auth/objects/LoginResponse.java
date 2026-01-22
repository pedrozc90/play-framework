package web.controllers.auth.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import core.utils.jackson.TimestampDeserializer;
import core.utils.jackson.TimestampSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class LoginResponse implements Serializable {

    @JsonProperty(value = "token")
    private final String token;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @JsonProperty(value = "issued_at")
    private final Instant issuedAt;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @JsonProperty(value = "expires_at")
    private final Instant expiresAt;

}
