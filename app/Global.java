import actors.ActorsManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.play.filters.CorsFilter;
import core.play.filters.LogginFilter;
import core.utils.jackson.ObjectMapperProvider;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.mvc.EssentialFilter;
import play.libs.F;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

public class Global extends GlobalSettings {

    private final Logger.ALogger logger = Logger.of(Global.class);
    private final ActorsManager manager = ActorsManager.getInstance();

    @Override
    public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{ CorsFilter.class, LogginFilter.class };
    }

    @Override
    public void onStart(final Application application) {
        logger.info("Application starting...");

        // configurations
        configureObjectMapper();

        // initialize actor system
        manager.init();
    }

    @Override
    public void onStop(final Application application) {
        logger.info("Application shutdown...");
    }

    @Override
    public F.Promise<Result> onBadRequest(final Http.RequestHeader request, final String error) {
        return super.onBadRequest(request, error);
    }

    @Override
    public F.Promise<Result> onHandlerNotFound(final Http.RequestHeader request) {
        return super.onHandlerNotFound(request);
    }

    @Override
    public F.Promise<Result> onError(final Http.RequestHeader request, final Throwable cause) {
        return super.onError(request, cause);
    }

    private void configureObjectMapper() {
        // configure new ObjectMapper instance
        final ObjectMapper mapper = ObjectMapperProvider.createMapper();

        // set the Jackson ObjectMapper for Play JSON
        Json.setObjectMapper(mapper);
    }

}
