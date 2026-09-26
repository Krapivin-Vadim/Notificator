package org.vadim.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.vadim.validation.annotation.NotificationTimeValidate;

import java.time.Instant;
import java.time.OffsetDateTime;

@Slf4j
public class NotificationTimeValidator implements
        ConstraintValidator <NotificationTimeValidate, OffsetDateTime> {

    @Override
    public boolean isValid(OffsetDateTime value, ConstraintValidatorContext context) {
        return value.toInstant().isAfter(Instant.now());
    }
}
