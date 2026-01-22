package core.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import core.utils.DateUtils;

import java.io.IOException;
import java.time.Instant;

public class TimestampSerializer extends JsonSerializer<Instant> {

    @Override
    public void serialize(final Instant value, final JsonGenerator gen, final SerializerProvider provider) throws IOException, JsonProcessingException {
        final String formatted = DateUtils.toISOString(value);
        if (formatted == null) {
            gen.writeNull();
        } else {
            gen.writeString(formatted);
        }
    }

}
