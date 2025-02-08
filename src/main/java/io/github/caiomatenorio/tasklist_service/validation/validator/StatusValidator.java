package io.github.caiomatenorio.tasklist_service.validation.validator;

import io.github.caiomatenorio.tasklist_service.validation.annotation.ValidStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<ValidStatus, String> {
    private static final String STATUS_PATTERN = "^(todo|in_progress|done)$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(STATUS_PATTERN);
    }

}
