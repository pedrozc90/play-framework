package core.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import core.utils.DateUtils;

import java.io.IOException;
import java.time.Instant;

public class TimestampDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(final JsonParser p, final DeserializationContext cxt) throws IOException, JsonProcessingException {
        final String value = p.getValueAsString();
        if (value == null || value.isEmpty()) return null;
        return DateUtils.toInstant(value);
    }

}
