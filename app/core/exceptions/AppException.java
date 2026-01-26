package core.exceptions;

import core.play.utils.ResultBuilder;
import core.utils.http.HttpStatus;
import core.utils.validation.Violation;
import lombok.Getter;
import play.mvc.Result;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.concurrent.CompletionException;

@Getter
public class AppException extends Exception {

    private final HttpStatus status;
    private final List<Violation> violations;

    private AppException(final HttpStatus status, final String message, final Throwable cause, final List<Violation> violations) {
        super(message, cause);
        this.status = status;
        this.violations = violations;
    }

    public static AppException of(final HttpStatus status, final String message) {
        return new AppException(status, message, null, null);
    }

    public static AppException of(final String message) {
        return of(HttpStatus.BAD_REQUEST, message);
    }

    public static AppException of(final HttpStatus status, final String fmt, final Object... args) {
        final String message = String.format(fmt, args);
        return of(status, message);
    }

    public static AppException of(final String fmt, final Object... args) {
        final String message = String.format(fmt, args);
        return of(HttpStatus.BAD_REQUEST, message);
    }

    public static AppException of(final HttpStatus status, final Throwable cause, final String message) {
        return new AppException(status, message, cause, null);
    }

    public static AppException of(final Throwable cause, final String fmt, final Object... args) {
        final String message = String.format(fmt, args);
        return of(HttpStatus.BAD_REQUEST, cause, message);
    }

    public static AppException of(ConstraintViolationException cause, List<Violation> violations) {
        return new AppException(HttpStatus.BAD_REQUEST, "Oops! Some of the information you entered is incorrect", cause, violations);
    }

    public Result toResult() {
        final ResultBuilder.AfterMessage build = ResultBuilder.of(getMessage(), violations);
        if (status == null) {
            return build.badRequest();
        }
        return build.status(status.getCode()).toResult();
    }

    public CompletionException toCompletionException() {
        return new CompletionException(this);
    }

}
