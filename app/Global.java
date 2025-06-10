import actors.ActorManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Json;

import java.text.SimpleDateFormat;

public class Global extends GlobalSettings {

    private static final Logger.ALogger logger = Logger.of(Global.class);

    @Override
    public void onStart(Application application) {
        logger.info("Application starting...");
        configureObjectMapper();
        ActorManager.init();
    }

    @Override
    public void onStop(Application application) {
        logger.info("Application shutdown...");
    }

    private void configureObjectMapper() {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        final ObjectMapper mapper = new ObjectMapper()
            .registerModule(javaTimeModule)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Json.setObjectMapper(mapper);
    }

}