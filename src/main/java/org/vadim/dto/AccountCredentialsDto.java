package org.vadim.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import org.vadim.validation.annotation.AccountCredentialsAuthValidate;

@AccountCredentialsAuthValidate(
    groups = AccountCredentialsDto.Auth.class, message = "Username and email cannot be blank simultaneously")
public record AccountCredentialsDto(

    @NotBlank(groups = Register.class)
    String username,

    @NotBlank(groups = Register.class)
    @Email
    String email,

    String tg,

    @NotBlank
    String password
) {
  public interface Auth extends Default{};
  public  interface Register extends Default{};
}
