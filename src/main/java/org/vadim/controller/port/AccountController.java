package org.vadim.controller.port;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vadim.dto.AccountCredentialsDto;
import org.vadim.dto.AuthResponseDto;

@RestController
@RequestMapping("/account")
public interface AccountController {
    @PostMapping("/reg")
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешная регистрация пользователя",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Пользователь с таким username/email уже есть"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации"
            )
    })
    ResponseEntity<AuthResponseDto> register(
            @RequestBody @Validated(AccountCredentialsDto.Register.class) AccountCredentialsDto accountCredentialsDto);

    @PostMapping("/auth")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно авторизован",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Неверные учетные данные пользователя"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации"
            )
    })
    ResponseEntity<AuthResponseDto> auth(
            @RequestBody @Validated(AccountCredentialsDto.Auth.class) AccountCredentialsDto accountCredentialsDto);
}
