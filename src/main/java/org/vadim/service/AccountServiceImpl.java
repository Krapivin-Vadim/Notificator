package org.vadim.service;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.vadim.config.security.port.JwtService;
import org.vadim.dto.AccountCredentialsDto;
import org.vadim.dto.AuthResponseDto;
import org.vadim.entity.Account;
import org.vadim.exception.EmailAlreadyUsedException;
import org.vadim.exception.FailedAuthenticationException;
import org.vadim.exception.FailedEmailAuthenticationException;
import org.vadim.exception.FailedUsernameAuthenticationException;
import org.vadim.exception.UsernameAlreadyUsedException;
import org.vadim.repository.AccountRepository;
import org.vadim.service.port.AccountService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Transactional
  @Override
  public AuthResponseDto auth(AccountCredentialsDto credentials) {
    if (StringUtils.hasText(credentials.username())){
      return authByUsername(credentials);
    }
    return authByEmail(credentials);
  }

  private AuthResponseDto authByEmail(AccountCredentialsDto credentials){
    return auth(new Credentials(credentials.email(), credentials.password()),
        accountRepository::findByEmail,
        FailedEmailAuthenticationException::new
    );
  }

  private AuthResponseDto authByUsername(AccountCredentialsDto credentials){
    return auth(new Credentials(credentials.username(), credentials.password()),
        accountRepository::findByUsername,
        FailedUsernameAuthenticationException::new
        );
  }

  private AuthResponseDto auth(Credentials credentials,
      Function<String, Optional<Account>> searcher,
      Supplier<? extends FailedAuthenticationException> authenticationExceptionClass){
    var accOpt = searcher.apply(credentials.identifier);
    if (accOpt.isEmpty()){
      log.warn("Wrong identifier in login attempt for identifier={}", credentials.identifier);
      throw authenticationExceptionClass.get();
    }
    var acc = accOpt.get();
    if (!passwordEncoder.matches(credentials.password,
        acc.getPasswordHash())
    ){
      log.warn("Wrong password in login attempt for identifier={}", credentials.identifier);
      throw authenticationExceptionClass.get();
    }
    log.info("Success login attempt for identifier={}", credentials.identifier);
    // todo: You need to return JWT
    return new AuthResponseDto(
            jwtService.generateToken(acc.getId().toString())
    );
  }

  @Transactional
  @Override
  public AuthResponseDto register(AccountCredentialsDto accountCredentials) {
    var username = accountCredentials.username();
    var email = accountCredentials.email();
    if(accountRepository.findByUsername(username).isPresent()){
      throw new UsernameAlreadyUsedException(username);
    }
    if(accountRepository.findByEmail(email).isPresent()){
      throw new EmailAlreadyUsedException(email);
    }
    var acc = new Account();
    acc.setUsername(username);
    acc.setEmail(email);
    acc.setPasswordHash(passwordEncoder.encode(accountCredentials.password()));
    acc.setTelegram(accountCredentials.tg());
    accountRepository.save(acc);
    return new AuthResponseDto(jwtService.generateToken(acc.getId().toString()));
  }

  private record Credentials(
      String identifier,
      String password
  ){}
}
