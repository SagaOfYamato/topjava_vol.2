package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.stereotype.Repository;

import javax.validation.*;
import java.util.Set;

@Repository
public class AbstractJdbcRepository {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    public void validate(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
