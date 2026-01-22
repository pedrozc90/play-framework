package core.utils.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import play.Logger;

import java.text.SimpleDateFormat;

public class ObjectMapperProvider {

    private static final Logger.ALogger logger = Logger.of(ObjectMapperProvider.class);

    public static ObjectMapper createMapper() {
        final ObjectMapper mapper = new ObjectMapper()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        logger.debug("ObjectMapper created successfully");
        return mapper;
    }

}
