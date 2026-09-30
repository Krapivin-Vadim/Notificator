package org.vadim.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record TelegramConnectDto (
    @Schema(
            name = "telegram tag",
            description = "Тэг бота в телеграм",
            example = "@Botname"
    )
    String botName,

    @Schema(
            name = "authentication token",
            description = "Временный токен аутентификации телеграм аккаунта"
    )
    UUID authenticationToken
){}
