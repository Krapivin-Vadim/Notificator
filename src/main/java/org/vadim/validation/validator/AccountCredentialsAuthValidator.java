package org.vadim.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;
import org.vadim.dto.AccountCredentialsDto;
import org.vadim.validation.annotation.AccountCredentialsAuthValidate;

public class AccountCredentialsAuthValidator implements
    ConstraintValidator<AccountCredentialsAuthValidate, AccountCredentialsDto> {

  @Override
  public boolean isValid(AccountCredentialsDto accountCredentialsDto, ConstraintValidatorContext constraintValidatorContext) {
    return Stream.of(accountCredentialsDto.username(), accountCredentialsDto.email())
        .anyMatch(StringUtils::hasText);
  }
}
