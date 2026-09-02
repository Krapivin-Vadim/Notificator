package org.vadim.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.vadim.validation.validator.TelegramTagValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TelegramTagValidator.class)
public @interface TelegramTagValidate {
  String message() default "Invalid telegram tag";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
