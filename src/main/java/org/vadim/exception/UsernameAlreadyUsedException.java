package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyUsedException extends NotificationServiceException{

  private static final String MSG = "Account with username %s already used";

  public UsernameAlreadyUsedException(String username) {
    super(String.format(MSG, username), HttpStatus.CONFLICT);
  }
}
