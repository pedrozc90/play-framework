package core.utils.validation;

import core.exceptions.AppException;
import core.exceptions.ExceptionMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Singleton
public class Validator {

    private final ExceptionMapper mapper;
    private final javax.validation.Validator _validator;

    @Inject
    public Validator(final ExceptionMapper mapper) {
        this.mapper = mapper;
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        _validator = factory.getValidator();
    }

    public <T> void validate(final T obj) throws AppException {
        final Set<ConstraintViolation<T>> violations = _validator.validate(obj);
        if (violations.isEmpty()) return;
        final ConstraintViolationException cause = new ConstraintViolationException(violations);
        throw mapper.toAppException(cause);
    }

}
