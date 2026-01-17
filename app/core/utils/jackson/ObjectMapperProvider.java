package core.utils.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import play.Logger;

import java.text.SimpleDateFormat;

public class ObjectMapperProvider {

    private static final Logger.ALogger logger = Logger.of(ObjectMapperProvider.class);

    public static ObjectMapper createMapper() {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        final ObjectMapper mapper = new ObjectMapper()
            .registerModule(javaTimeModule)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        logger.debug("ObjectMapper created successfully");
        return mapper;
    }

}
