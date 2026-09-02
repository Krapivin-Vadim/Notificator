package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyUsedException extends NotificationServiceException {

  private static final String MSG = "Account with email %s already used";

  public EmailAlreadyUsedException(String email) {
    super(MSG.formatted(email), HttpStatus.CONFLICT);
  }
}
