package core.utils.validation;

import core.exceptions.AppException;
import core.exceptions.ExceptionMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Singleton
public class Validator {

    private final ExceptionMapper mapper;
    private final javax.validation.Validator _validator;

    @Inject
    public Validator(
        final ExceptionMapper mapper,
        final javax.validation.Validator validator
    ) {
        this.mapper = mapper;
        this._validator = validator;
    }

    public <T> void validate(final T obj) throws AppException {
        final Set<ConstraintViolation<T>> violations = _validator.validate(obj);
        if (violations.isEmpty()) return;
        final ConstraintViolationException cause = new ConstraintViolationException(violations);
        throw mapper.toAppException(cause);
    }

}
