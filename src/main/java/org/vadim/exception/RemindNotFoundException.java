package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class RemindNotFoundException extends NotificationServiceException {

    private static final String MSG = "Remind with id %s not found";

    public RemindNotFoundException(Long remindId) {
        super(MSG.formatted(remindId), HttpStatus.NOT_FOUND);
    }
}
