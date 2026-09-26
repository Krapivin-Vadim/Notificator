package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class RemindNotFounfException extends NotificationServiceException {

    private static final String MSG = "Remind with id %s not found";

    public RemindNotFounfException(Long remindId) {
        super(MSG.formatted(remindId), HttpStatus.NOT_FOUND);
    }
}
