package core.play.handlers;

import core.exceptions.AppException;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;

public class ExceptionHandler {

    public F.Promise<Result> onBadRequest(final Http.RequestHeader request, final String error) {
        return null;
    }

    public F.Promise<Result> onHandlerNotFound(final Http.RequestHeader request) {
        return null;
    }

    public F.Promise<Result> onError(final Http.RequestHeader request, final Throwable exception) {
        if (exception instanceof AppException) {
            // ignore
        }

        final Throwable cause = exception.getCause();
        if (cause != null) {
            if (cause instanceof AppException) {
                final AppException exp = (AppException) cause;
                return F.Promise.pure(exp.toResult());
            }
        }

        return null;
    }

}
