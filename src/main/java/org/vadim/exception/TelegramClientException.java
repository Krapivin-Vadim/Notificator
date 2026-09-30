package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class TelegramClientException extends NotificationServiceException {
    private static final String MSG = "Unable to send message";
    public TelegramClientException() {
        super(MSG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
