package io.github.caiomatenorio.tasklist_service.validation.validator;

import io.github.caiomatenorio.tasklist_service.validation.annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]{3,255}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(USERNAME_PATTERN);
    }
}
