package core.utils.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import play.Logger;

import java.text.SimpleDateFormat;

public class ObjectMapperProvider {

    private static final Logger.ALogger logger = Logger.of(ObjectMapperProvider.class);

    public static ObjectMapper createMapper() {
         final JSR310Module jsr310Module = new JSR310Module();
        final Jdk8Module jdk8Module = new Jdk8Module();
        final ObjectMapper mapper = new ObjectMapper()
            .registerModule(jsr310Module)
            .registerModule(jdk8Module)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        logger.debug("ObjectMapper created successfully");
        return mapper;
    }

}
