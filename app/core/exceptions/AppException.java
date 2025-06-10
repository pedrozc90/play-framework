package core.exceptions;

public class AppException extends Exception {

    private AppException(final String message) {
        super(message);
    }

    private AppException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static AppException of(final String message) {
        return new AppException(message);
    }

    public static AppException of(final String fmt, final Object... args) {
        final String message = String.format(fmt, args);
        return new AppException(message);
    }

    public static AppException of(final Throwable cause, final String fmt, final Object... args) {
        final String message = String.format(fmt, args);
        return new AppException(message, cause);
    }

}
