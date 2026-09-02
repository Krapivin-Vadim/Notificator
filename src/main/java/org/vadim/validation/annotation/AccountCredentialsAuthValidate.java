package org.vadim.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.vadim.validation.validator.AccountCredentialsAuthValidator;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountCredentialsAuthValidator.class)
public @interface AccountCredentialsAuthValidate {
  String message() default "No authenticator";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
