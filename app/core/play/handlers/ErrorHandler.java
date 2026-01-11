package core.play.handlers;

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

public class ErrorHandler extends DefaultHttpErrorHandler {

    @Inject
    public ErrorHandler(final Configuration configuration,
                        final Environment environment,
                        final OptionalSourceMapper sourceMapper,
                        final Provider<Router> routes) {
        super(configuration, environment, sourceMapper, routes);
    }

    @Override
    protected F.Promise<Result> onBadRequest(final Http.RequestHeader request, final String message) {
        return super.onBadRequest(request, message);
    }

    @Override
    protected F.Promise<Result> onForbidden(final Http.RequestHeader request, final String message) {
        return super.onForbidden(request, message);
    }

    @Override
    protected F.Promise<Result> onNotFound(final Http.RequestHeader request, final String message) {
        return super.onNotFound(request, message);
    }

    @Override
    public F.Promise<Result> onServerError(final Http.RequestHeader request, final Throwable cause) {
        return super.onServerError(request, cause);
    }

    @Override
    public F.Promise<Result> onClientError(final Http.RequestHeader request, final int statusCode, final String message) {
        return super.onClientError(request, statusCode, message);
    }

    @Override
    protected F.Promise<Result> onOtherClientError(final Http.RequestHeader request, final int statusCode, final String message) {
        return super.onOtherClientError(request, statusCode, message);
    }

    @Override
    protected void logServerError(final Http.RequestHeader request, final UsefulException exception) {
        super.logServerError(request, exception);
    }

    @Override
    protected F.Promise<Result> onDevServerError(final Http.RequestHeader request, final UsefulException exception) {
        return super.onDevServerError(request, exception);
    }

    @Override
    protected F.Promise<Result> onProdServerError(final Http.RequestHeader request, final UsefulException exception) {
        return super.onProdServerError(request, exception);
    }

}
