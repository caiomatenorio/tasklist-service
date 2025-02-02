package io.github.caiomatenorio.tasklist_service.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.caiomatenorio.tasklist_service.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "The password must have at least 8 characters, 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
