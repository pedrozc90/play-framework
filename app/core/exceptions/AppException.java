package core.exceptions;

import core.objects.ErrorMessage;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionException;

public class AppException extends Exception {

    private final int status;

    private AppException(final Integer status, final Throwable cause, final String message) {
        super(message, cause);
        this.status = (status != null) ? status : 400;
    }

    private AppException(final int status, final String message) {
        this(status, null, message);
    }

    private AppException(final String message) {
        this(null, null, message);
    }

    private AppException(final String message, final Throwable cause) {
        this(400, cause, message);
    }

    public static AppException of(final int status, final String message) {
        return new AppException(status, message);
    }

    public static AppException of(final String message) {
        return new AppException(400, message);
    }

    public static AppException of(final String fmt, final Object... args) {
        final String message = String.format(fmt, args);
        return new AppException(message);
    }

    public static AppException of(final Throwable cause, final String fmt, final Object... args) {
        final String message = String.format(fmt, args);
        return new AppException(message, cause);
    }

    public Result toResult() {
        final ErrorMessage obj = new ErrorMessage(getMessage());
        return Results.status(status, Json.toJson(obj));
    }

    public CompletionException toCompletionException() {
        return new CompletionException(this);
    }

}
