package core.play.handlers;

import core.exceptions.AppException;
import core.play.utils.ResultBuilder;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

public class ExceptionHandler {

    private static ExceptionHandler instance;

    public static ExceptionHandler getInstance() {
        if (instance == null) {
            instance = new ExceptionHandler();
        }
        return instance;
    }

    public F.Promise<Result> onBadRequest(final Http.RequestHeader request, final String error) {
        return F.Promise.pure(
            ResultBuilder.of()
                .message(error)
                .badRequest()
        );
    }

    public F.Promise<Result> onHandlerNotFound(final Http.RequestHeader request) {
        final String method = request.method();
        final String path = request.path();
        return F.Promise.pure(
            ResultBuilder.of()
                .message("Resource %s %s not found", method, path)
                .notFound()
        );
    }

    public F.Promise<Result> onError(final Http.RequestHeader request, final Throwable exception) {
        final AppException cause = captureException(exception, AppException.class);
        if (cause != null) {
            return F.Promise.pure(cause.toResult());
        }

        return F.Promise.pure(
            ResultBuilder.of()
                .message(exception.getMessage())
                .internalServerError()
        );
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
