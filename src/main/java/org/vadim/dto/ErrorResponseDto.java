package org.vadim.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ErrorResponseDto(
        @Schema(
                description = "Сообщение об ошибке",
                example = "Account with email user@example.com already used"
        )
        String message,

        @Schema(
                description = "Детали ошибки (опционально)"
        )
        List<ErrorDetail> details
) {
    public ErrorResponseDto(String message){
        this(message, null);
    }

    public ErrorResponseDto(String message, List<ErrorDetail> details) {
        this.message = message;
        this.details = details;
    }
}
