package org.vadim.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import org.vadim.validation.annotation.AccountCredentialsAuthValidate;
import org.vadim.validation.annotation.TelegramTagValidate;

@AccountCredentialsAuthValidate(
    groups = AccountCredentialsDto.Auth.class, message = "Username and email cannot be blank simultaneously")
public record AccountCredentialsDto(

    @NotBlank(groups = Register.class)
    @Schema(
        description = "Имя пользователя",
        example = "User"
    )
    String username,

    @NotBlank(groups = Register.class)
    @Email
    @Schema(
        description = "Адрес электронной почты пользователя",
        example = "user@example.com"
    )
    String email,

    @TelegramTagValidate(groups = Register.class)
    @Schema(
        description = "Тэг пользователя в Telegram",
        example = "@User"
    )
    String tg,

    @NotBlank
    @Schema(
        description = "Пароль пользователя",
        example = "user123"
    )
    String password
) {
  public interface Auth extends Default{};
  public  interface Register extends Default{};
}
