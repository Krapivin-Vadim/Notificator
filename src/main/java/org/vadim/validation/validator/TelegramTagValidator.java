package org.vadim.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.vadim.validation.annotation.TelegramTagValidate;

public class TelegramTagValidator implements
    ConstraintValidator <TelegramTagValidate, String> {

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return s.startsWith("@");
  }
}
