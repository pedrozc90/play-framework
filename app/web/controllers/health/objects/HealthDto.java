package web.controllers.health.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import core.utils.jackson.TimestampDeserializer;
import core.utils.jackson.TimestampSerializer;
import core.utils.jackson.ZoneIdDeserializer;
import core.utils.jackson.ZoneIdSerializer;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;

@Data
public class HealthDto {

    @JsonProperty(value = "name", required = true)
    private final String name;

    @JsonProperty(value = "version")
    private final String version;

    @JsonProperty(value = "mode")
    private final String mode;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @JsonProperty(value = "timestamp")
    private final Instant timestamp = Instant.now();

    @JsonSerialize(using = ZoneIdSerializer.class)
    @JsonDeserialize(using = ZoneIdDeserializer.class)
    @JsonProperty(value = "timezone")
    private final ZoneId timezone = ZoneId.systemDefault();

}
