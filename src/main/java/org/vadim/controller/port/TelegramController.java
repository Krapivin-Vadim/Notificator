package org.vadim.controller.port;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.vadim.dto.ErrorResponseDto;
import org.vadim.dto.TelegramConnectDto;

import java.util.UUID;

public interface TelegramController {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное создание временного токена аутентификации в телеграм",
                    content = @Content(schema = @Schema(implementation = TelegramConnectDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/connect")
    TelegramConnectDto connectTelegram();
}
