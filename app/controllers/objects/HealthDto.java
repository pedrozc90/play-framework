package controllers.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import utils.ZoneIdDeserializer;
import utils.ZoneIdSerializer;

import java.time.Instant;
import java.time.ZoneId;

public class HealthDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty(value = "timestamp")
    private Instant timestamp = Instant.now();

    @JsonSerialize(using = ZoneIdSerializer.class)
    @JsonDeserialize(using = ZoneIdDeserializer.class)
    @JsonProperty(value = "timezone")
    private ZoneId timezone = ZoneId.systemDefault();

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public ZoneId getTimezone() {
        return timezone;
    }

    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }

}
