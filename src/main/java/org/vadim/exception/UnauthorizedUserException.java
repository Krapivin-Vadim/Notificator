package org.vadim.exception;

public class UnauthorizedUserException extends RuntimeException {
    private static String MSG = "User is unauthorized";
    public UnauthorizedUserException() {
        super(MSG);
    }
}
