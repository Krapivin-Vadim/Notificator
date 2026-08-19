package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class FailedAuthenticationException extends NotificationServiceException {
  public FailedAuthenticationException(String message) {
    super(message, HttpStatus.FORBIDDEN);
  }
}
