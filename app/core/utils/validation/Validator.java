package core.utils.validation;

import core.exceptions.AppException;
import core.exceptions.ExceptionMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class Validator {

    private final ExceptionMapper mapper = ExceptionMapper.getInstance();

    private final javax.validation.Validator _validator;

    public Validator() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        _validator = factory.getValidator();
    }

    private static Validator instance;

    public static Validator getInstance() {
        if (instance == null) {
            instance = new Validator();
        }
        return instance;
    }

    public <T> void validate(final T obj) throws AppException {
        final Set<ConstraintViolation<T>> violations = _validator.validate(obj);
        if (violations.isEmpty()) return;
        final ConstraintViolationException cause = new ConstraintViolationException(violations);
        throw mapper.toAppException(cause);
    }

}
