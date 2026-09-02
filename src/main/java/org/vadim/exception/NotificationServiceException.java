package org.vadim.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotificationServiceException extends RuntimeException {

  @Getter
  private final HttpStatus httpStatus;

  public NotificationServiceException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
