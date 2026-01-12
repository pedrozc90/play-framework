package core.play.handlers;

import com.typesafe.config.Config;
import core.exceptions.AppException;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public ErrorHandler(final Config config,
                        final Environment environment,
                        final OptionalSourceMapper sourceMapper,
                        final Provider<Router> routes) {
        super(config, environment, sourceMapper, routes);
    }

    @Override
    protected CompletionStage<Result> onBadRequest(final Http.RequestHeader request, final String message) {
        return super.onBadRequest(request, message);
    }

    @Override
    protected CompletionStage<Result> onForbidden(final Http.RequestHeader request, final String message) {
        return super.onForbidden(request, message);
    }

    @Override
    protected CompletionStage<Result> onNotFound(final Http.RequestHeader request, final String message) {
        return super.onNotFound(request, message);
    }

    @Override
    public CompletionStage<Result> onClientError(final Http.RequestHeader request, final int statusCode, final String message) {
        return super.onClientError(request, statusCode, message);
    }

    @Override
    protected CompletionStage<Result> onOtherClientError(final Http.RequestHeader request, final int statusCode, final String message) {
        return super.onOtherClientError(request, statusCode, message);
    }

    @Override
    public CompletionStage<Result> onServerError(final Http.RequestHeader request, final Throwable exception) {
        final Throwable cause = unwrap(exception);

        if (cause instanceof AppException) {
            AppException e = (AppException) cause;
            return CompletableFuture.completedFuture(e.toResult());
        }

        return super.onServerError(request, exception);
    }

    private Throwable unwrap(final Throwable throwable) {
        if (throwable instanceof CompletionException && throwable.getCause() != null) {
            return throwable.getCause();
        }
        return throwable;
    }

}
