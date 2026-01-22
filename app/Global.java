import actors.ActorsManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.play.filters.CorsFilter;
import core.play.filters.LogginFilter;
import core.play.handlers.ExceptionHandler;
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
    private final ExceptionHandler handler = ExceptionHandler.getInstance();

    @Override
    public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{ CorsFilter.class, LogginFilter.class };
    }

    @Override
    public void onStart(final Application application) {
        logger.info("Application starting...");

        // configure new ObjectMapper instance
        final ObjectMapper mapper = ObjectMapperProvider.createMapper();

        // set the Jackson ObjectMapper for Play JSON
        Json.setObjectMapper(mapper);

        // initialize actor system
        manager.init();
    }

    @Override
    public void onStop(final Application application) {
        logger.info("Application shutdown...");
    }

    @Override
    public F.Promise<Result> onBadRequest(final Http.RequestHeader request, final String error) {
        return handler.onBadRequest(request, error);
    }

    @Override
    public F.Promise<Result> onHandlerNotFound(final Http.RequestHeader request) {
        return handler.onHandlerNotFound(request);
    }

    @Override
    public F.Promise<Result> onError(final Http.RequestHeader request, final Throwable cause) {
        return handler.onError(request, cause);
    }

}
