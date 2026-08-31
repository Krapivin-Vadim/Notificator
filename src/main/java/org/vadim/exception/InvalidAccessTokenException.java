package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class InvalidAccessTokenException extends NotificationServiceException {
  public InvalidAccessTokenException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
