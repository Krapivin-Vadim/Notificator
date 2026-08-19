package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class NotificationServiceException extends RuntimeException {

  private final HttpStatus httpStatus;

  public NotificationServiceException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
