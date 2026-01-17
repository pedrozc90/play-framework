package core.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import core.exceptions.AppException;
import core.utils.http.HttpStatus;
import core.utils.validation.Violation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Field;

public class ExceptionMapper {

    private static ExceptionMapper instance;

    public static ExceptionMapper getInstance() {
        if (instance == null) {
            instance = new ExceptionMapper();
        }
        return instance;
    }

    public Violation of(final ConstraintViolation<?> violation) {
        final String field = getField(violation);
        final String message = getMessage(violation);
        return new Violation(field, message, null);
    }

    private String getMessage(final ConstraintViolation<?> violation) {
        return violation.getMessage();
    }

    private String getField(final ConstraintViolation<?> violation) {
        final String[] segments = violation.getPropertyPath().toString().split("\\.");
        final String field = segments[segments.length - 1];
        return getJsonPropertyName(violation.getRootBeanClass(), field);
    }

    private String getJsonPropertyName(final Class<?> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            final JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (jsonProperty != null && !jsonProperty.value().isEmpty()) {
                return jsonProperty.value();
            }
        } catch (NoSuchFieldException e) {
            // Field not found, return null
        }
        return fieldName;
    }

    public AppException toAppException(final ConstraintViolationException cause) {
        return AppException.of(HttpStatus.BAD_REQUEST, "Oops! Some of the information you entered is incorrect", cause);
    }

}
