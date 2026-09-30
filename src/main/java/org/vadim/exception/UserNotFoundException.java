package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends NotificationServiceException {
    private static final String MSG = "User with id %s not found";
    public UserNotFoundException(Long userId) {
        super(MSG.formatted(userId), HttpStatus.BAD_REQUEST);
    }
}
