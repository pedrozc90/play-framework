package core.play.handlers;

import core.exceptions.AppException;
import core.play.utils.ResultBuilder;
import play.Configuration;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.UsefulException;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ExceptionHandler extends DefaultHttpErrorHandler {

    @Inject
    public ExceptionHandler(
        Configuration configuration,
        Environment environment,
        OptionalSourceMapper sourceMapper,
        Provider<Router> routes
    ) {
        super(configuration, environment, sourceMapper, routes);
    }

    @Override
    protected F.Promise<Result> onBadRequest(final Http.RequestHeader request, final String message) {
        return F.Promise.pure(ResultBuilder.of().message(message).badRequest());
    }

    @Override
    protected F.Promise<Result> onForbidden(final Http.RequestHeader request, final String message) {
        return F.Promise.pure(ResultBuilder.of().message(message).forbidden());
    }

    @Override
    protected F.Promise<Result> onNotFound(final Http.RequestHeader request, final String message) {
        return F.Promise.pure(ResultBuilder.of().message(message).notFound());
    }

    @Override
    protected F.Promise<Result> onOtherClientError(final Http.RequestHeader request, final int status, final String message) {
        return F.Promise.pure(ResultBuilder.of().message(message).status(status).toResult());
    }

    @Override
    public F.Promise<Result> onServerError(final Http.RequestHeader request, final Throwable exception) {
        final AppException captured = captureException(exception, AppException.class);
        if (captured != null) {
            return F.Promise.pure(captured.toResult());
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
