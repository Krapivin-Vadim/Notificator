package org.vadim.exception;

public class FailedUsernameAuthenticationException extends FailedAuthenticationException {
  public FailedUsernameAuthenticationException() {
    super("Wrong username or password");
  }
}
