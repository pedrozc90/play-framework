package core.exceptions;

import core.utils.http.HttpStatus;
import core.play.utils.ResultBuilder;
import lombok.Getter;
import play.mvc.Result;

@Getter
public class AppException extends Exception {

    private final HttpStatus status;

    private AppException(final HttpStatus status, final String message, final Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public static AppException of(final HttpStatus status, final String message) {
        return new AppException(status, message, null);
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

    public static AppException of(final Throwable cause, final String fmt, final Object... args) {
        final String message = String.format(fmt, args);
        return new AppException(HttpStatus.BAD_REQUEST, message, cause);
    }

    public Result toResult() {
        return ResultBuilder
            .of(this.getMessage())
            .status(status.getCode());
    }

}
