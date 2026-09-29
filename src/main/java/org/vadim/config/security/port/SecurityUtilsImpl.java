package org.vadim.config.security.port;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.vadim.entity.Account;
import org.vadim.exception.UnauthorizedUserException;

import java.util.Optional;

@Component
public class SecurityUtilsImpl implements SecurityUtils{
    @Override
    public Long getAccountIdFromToken() {
        return getUserDetails().map(Account::getId)
                .orElseThrow(UnauthorizedUserException::new);
    }

    private Optional<Account> getUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof Account account){
            return Optional.of(account);
        }
        return Optional.empty();
    }
}
