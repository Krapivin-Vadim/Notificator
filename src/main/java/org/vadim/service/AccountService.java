package org.vadim.service;

import org.vadim.dto.AccountCredentialsDto;
import org.vadim.dto.AuthResponseDto;

public interface AccountService {
  public AuthResponseDto auth(AccountCredentialsDto accountCredentials);

  public AuthResponseDto register(AccountCredentialsDto accountCredentials);
}
