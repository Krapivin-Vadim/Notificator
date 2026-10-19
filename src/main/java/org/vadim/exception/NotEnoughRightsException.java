package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class NotEnoughRightsException extends NotificationServiceException {
    private static final String MSG = "Not enough rights to perform the action";
    public NotEnoughRightsException() {
        super(MSG, HttpStatus.FORBIDDEN);
    }
}
