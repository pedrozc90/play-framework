package core.play.handlers;

import com.typesafe.config.Config;
import core.exceptions.AppException;
import core.play.utils.ResultBuilder;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ExceptionHandler extends DefaultHttpErrorHandler {

    @Inject
    public ExceptionHandler(
        final Config config,
        final Environment environment,
        final OptionalSourceMapper sourceMapper,
        final Provider<Router> routes
    ) {
        super(config, environment, sourceMapper, routes);
    }

    @Override
    protected CompletionStage<Result> onBadRequest(final Http.RequestHeader request, final String message) {
        return CompletableFuture.completedFuture(ResultBuilder.of().message(message).badRequest());
    }

    @Override
    protected CompletionStage<Result> onForbidden(final Http.RequestHeader request, final String message) {
        return CompletableFuture.completedFuture(ResultBuilder.of().message(message).forbidden());
    }

    @Override
    protected CompletionStage<Result> onNotFound(final Http.RequestHeader request, final String message) {
        return CompletableFuture.completedFuture(ResultBuilder.of().message(message).notFound());
    }

    @Override
    protected CompletionStage<Result> onOtherClientError(final Http.RequestHeader request, final int status, final String message) {
        return CompletableFuture.completedFuture(ResultBuilder.of().message(message).status(status).toResult());
    }

    @Override
    public CompletionStage<Result> onServerError(final Http.RequestHeader request, final Throwable exception) {
        final AppException captured = captureException(exception, AppException.class);
        if (captured != null) {
            return CompletableFuture.completedFuture(captured.toResult());
        }
        return super.onServerError(request, exception);
    }

    @Override
    protected void logServerError(final Http.RequestHeader request, final UsefulException usefulException) {
        super.logServerError(request, usefulException);
    }

    private <T extends Throwable> T captureException(final Throwable exception, final Class<T> clazz, final int depth) {
        if (exception == null || depth <= 0) return null;
        if (clazz.isInstance(exception)) return clazz.cast(exception);
        return captureException(exception.getCause(), clazz, depth - 1);
    }

    private <T extends Throwable> T captureException(final Throwable exception, final Class<T> clazz) {
        return captureException(exception, clazz, 5);
    }

}
