package org.vadim.app;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vadim.dto.AccountCredentialsDto;
import org.vadim.entity.Account;
import org.vadim.exception.EmailAlreadyUsedException;
import org.vadim.exception.FailedEmailAuthenticationException;
import org.vadim.exception.FailedUsernameAuthenticationException;
import org.vadim.exception.UsernameAlreadyUsedException;
import org.vadim.repository.AccountRepository;
import org.vadim.service.AccountServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
  private final AccountCredentialsDto testCredetials = new AccountCredentialsDto(
      "USER",
      "user@example.com",
      "@User",
      "user123"
  );

  private final Account testAccount = new Account(
      1L, testCredetials.username(), testCredetials.email(), testCredetials.tg(), testCredetials.password()
  );

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountServiceImpl accountService;

  @Test
  void successfulRegistration(){
    Mockito.when(accountRepository.findByUsername(testCredetials.username())).thenReturn(Optional.empty());
    Mockito.when(accountRepository.findByEmail(testCredetials.email())).thenReturn(Optional.empty());
    accountService.register(testCredetials);
  }

  @Test()
  void registrationWithDuplicatedUsername(){
    Mockito.when(accountRepository.findByUsername(testCredetials.username())).thenReturn(Optional.of(testAccount));
    Assertions.assertThrows(UsernameAlreadyUsedException.class, () -> accountService.register(testCredetials));
  }

  @Test
  void registrationWithDuplicatedEmail(){
    Mockito.when(accountRepository.findByEmail(testCredetials.email())).thenReturn(Optional.of(testAccount));
    Assertions.assertThrows(EmailAlreadyUsedException.class, () -> accountService.register(testCredetials));
  }

  @Test
  void successAuthByUsername(){
    Mockito.when(accountRepository.findByUsername(testCredetials.username())).thenReturn(Optional.of(testAccount));
    Mockito.when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class)))
        .then(invocationOnMock -> {
          var rowPasswd = invocationOnMock.getArgument(0);
          var encodedPasswd = invocationOnMock.getArgument(1);
          return encodedPasswd.equals(rowPasswd);
        });
    accountService.auth(testCredetials);
  }

  @Test
  void noSuchUsername(){
    Mockito.when(accountRepository.findByUsername(testCredetials.username())).thenReturn(Optional.empty());
    Assertions.assertThrows(FailedUsernameAuthenticationException.class, () -> accountService.auth(testCredetials));
  }

  @Test
  void authByUsernameWithIncorrectPassword(){
    Mockito.when(accountRepository.findByUsername(testCredetials.username())).thenReturn(Optional.of(testAccount));
    Mockito.when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class)))
        .then(invocationOnMock -> {
          var rowPasswd = invocationOnMock.getArgument(0);
          var encodedPasswd = invocationOnMock.getArgument(1);
          return encodedPasswd.equals(rowPasswd);
        });
    Assertions.assertThrows(FailedUsernameAuthenticationException.class,
        () -> accountService.auth(new AccountCredentialsDto(
        testCredetials.username(),
        testCredetials.email(),
        testCredetials.tg(),
        "Another password"
    )));
  }

  @Test
  void successAuthByEmail(){
    Mockito.when(accountRepository.findByEmail(testCredetials.email())).thenReturn(Optional.of(testAccount));
    Mockito.when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class)))
        .then(invocationOnMock -> {
          var rowPasswd = invocationOnMock.getArgument(0);
          var encodedPasswd = invocationOnMock.getArgument(1);
          return encodedPasswd.equals(rowPasswd);
        });
    accountService.auth(getEmailAuthTestCredentials());
  }

  @Test
  void noSuchEmail(){
    Mockito.when(accountRepository.findByEmail(testCredetials.email())).thenReturn(Optional.empty());
    Assertions.assertThrows(FailedEmailAuthenticationException.class, () -> accountService.auth(getEmailAuthTestCredentials()));
  }

  @Test
  void authByEmailWithIncorrectPassword(){
    Mockito.when(accountRepository.findByEmail(testCredetials.email())).thenReturn(Optional.of(testAccount));
    Mockito.when(passwordEncoder.matches(Mockito.any(String.class), Mockito.any(String.class)))
        .then(invocationOnMock -> {
          var rowPasswd = invocationOnMock.getArgument(0);
          var encodedPasswd = invocationOnMock.getArgument(1);
          return encodedPasswd.equals(rowPasswd);
        });
    Assertions.assertThrows(FailedEmailAuthenticationException.class,
        () -> accountService.auth(getEmailAuthTestCredentials("Another password")));
  }

  private AccountCredentialsDto getEmailAuthTestCredentials(){
    return getEmailAuthTestCredentials(testCredetials.password());
  }

  private AccountCredentialsDto getEmailAuthTestCredentials(String newPassword){
    return new AccountCredentialsDto(
        null,
        testCredetials.email(),
        testCredetials.tg(),
        newPassword
    );
  }
}
