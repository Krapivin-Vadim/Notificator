package org.vadim.dto;

public record RemindCreationResponseDto(
        RemindCreationStatus emailStatus,
        RemindCreationStatus telegramStatus
) {
}
