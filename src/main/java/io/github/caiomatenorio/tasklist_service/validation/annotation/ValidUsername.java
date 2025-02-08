package io.github.caiomatenorio.tasklist_service.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.caiomatenorio.tasklist_service.validation.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UsernameValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default "The password must be between 8 and 255 characters long, and it must have 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
