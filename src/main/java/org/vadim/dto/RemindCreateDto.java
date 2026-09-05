package org.vadim.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import org.vadim.validation.annotation.NotificationTimeValidate;

import java.time.OffsetDateTime;

public record RemindCreateDto(

        @Schema(name = "Заголовок напоминания", example = "My simple remind")
        @NotBlank(groups = Default.class)
        String title,

        @Schema(name = "Текст/описание напоминания", example = "My very long remind description")
        String description,

        @Schema(name = "Время напоминания", example = "2026-09-05T00:01:00+03:00")
        @NotNull(message = "null value is not allowed", groups = NotNullGroup.class)
        @NotificationTimeValidate(groups = Default.class)
        OffsetDateTime remindAt
) {
        @GroupSequence({RemindCreateDto.NotNullGroup.class, Default.class})
        public interface RemindGroupSequenceValidation{}
        public interface NotNullGroup{}
}
