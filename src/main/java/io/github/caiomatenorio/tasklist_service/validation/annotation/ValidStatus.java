package io.github.caiomatenorio.tasklist_service.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.caiomatenorio.tasklist_service.validation.validator.StatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = StatusValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidStatus {
    String message() default "The status must be one of the following values: todo, in_progress, done";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
