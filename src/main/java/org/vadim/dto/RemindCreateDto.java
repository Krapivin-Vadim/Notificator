package org.vadim.dto;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import org.vadim.validation.annotation.NotificationTimeValidate;

import java.time.OffsetDateTime;


public record RemindCreateDto(

        @NotBlank(groups = Default.class)
        String title,

        String description,

        @NotNull(message = "null value is not allowed", groups = NotNullGroup.class)
        @NotificationTimeValidate(groups = Default.class)
        OffsetDateTime remindAt
) {
        @GroupSequence({RemindCreateDto.NotNullGroup.class, Default.class})
        public interface RemindGroupSequenceValidation{}
        public interface NotNullGroup{}
}
