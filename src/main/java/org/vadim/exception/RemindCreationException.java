package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class RemindCreationException extends NotificationServiceException {
    private static final String MSG = "Unable to create notifications for remind %s";
    public RemindCreationException(Long remindId) {
        super(MSG.formatted(remindId), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
