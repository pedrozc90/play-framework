import actors.ActorsManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.utils.jackson.ObjectMapperProvider;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;
import play.libs.Json;

import javax.inject.Inject;
import javax.inject.Singleton;

// This creates an `ApplicationStart` object once at start-up.
@Singleton
public class Entrypoint {

    private final Logger.ALogger logger = Logger.of(Entrypoint.class);

    // Inject the application's Environment upon start-up and register hook(s) for shut-down.
    @Inject
    public Entrypoint(
        final ApplicationLifecycle lifecycle,
        final ActorsManager manager
    ) {
        onStart(manager);
        onStop(lifecycle);
    }

    private void onStart(final ActorsManager manager) {
        logger.info("Application starting...");

        // configurations
        final ObjectMapper mapper = ObjectMapperProvider.createMapper();

        // Set the Jackson ObjectMapper for Play JSON
        Json.setObjectMapper(mapper);
    }

    private void onStop(final ApplicationLifecycle lifecycle) {
        lifecycle.addStopHook(() -> {
            logger.info("Application shutdown...");
            return F.Promise.pure(null);
        });
    }

}
