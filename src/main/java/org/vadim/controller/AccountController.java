package org.vadim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vadim.dto.AccountCredentialsDto;
import org.vadim.dto.AuthResponseDto;
import org.vadim.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

  private final AccountService accountService;

  @Autowired
  public  AccountController(AccountService accountService){
    this.accountService = accountService;
  }

  @PostMapping("/reg")
  public ResponseEntity<AuthResponseDto> register(
      @RequestBody @Validated(AccountCredentialsDto.Register.class) AccountCredentialsDto accountCredentialsDto){
    return ResponseEntity.status(HttpStatus.CREATED).body(
        accountService.register(accountCredentialsDto)
    );
  }

  @PostMapping("/auth")
  public ResponseEntity<AuthResponseDto> auth(
      @RequestBody @Validated(AccountCredentialsDto.Auth.class) AccountCredentialsDto accountCredentialsDto){
    return ResponseEntity.status(HttpStatus.OK).body(
        accountService.auth(accountCredentialsDto)
    );
  }
}
