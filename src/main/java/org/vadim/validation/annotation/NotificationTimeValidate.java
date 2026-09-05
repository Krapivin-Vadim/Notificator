package org.vadim.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.vadim.validation.validator.NotificationTimeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotificationTimeValidator.class)
public @interface NotificationTimeValidate {
    String message() default "Notification time must be later";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
