package core.utils.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZoneId;

public class ZoneIdSerializer extends JsonSerializer<ZoneId> {

    @Override
    public void serialize(final ZoneId value, final JsonGenerator gen, final SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value != null) {
            gen.writeString(value.getId());
        } else {
            gen.writeNull();
        }
    }

}
