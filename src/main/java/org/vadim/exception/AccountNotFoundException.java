package org.vadim.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends NotificationServiceException {
    private static final String MSG = "Account with id %s not found";
    public AccountNotFoundException(Long accountId) {
        super(MSG.formatted(accountId), HttpStatus.NOT_FOUND);
    }
}
