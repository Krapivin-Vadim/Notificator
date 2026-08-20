package org.vadim.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponseDto(
    @Schema(
        description = "JWT токен авторизации"
    )
    String token
) {
}
