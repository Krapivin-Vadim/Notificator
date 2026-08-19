package org.vadim.exception;

public class FailedEmailAuthenticationException extends FailedAuthenticationException {
  public FailedEmailAuthenticationException() {
    super("Wrong email or password");
  }
}
