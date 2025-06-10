package core.utils.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZoneId;

public class ZoneIdDeserializer extends JsonDeserializer<ZoneId> {

    @Override
    public ZoneId deserialize(final JsonParser parser, final DeserializationContext context) throws IOException, JsonProcessingException {
        final String value = parser.getText();
        return ZoneId.of(value);
    }

}
