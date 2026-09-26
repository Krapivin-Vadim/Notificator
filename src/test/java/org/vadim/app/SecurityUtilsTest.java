package org.vadim.app;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.vadim.config.security.JwtServiceImpl;
import org.vadim.config.security.port.JwtService;
import org.vadim.config.security.port.SecurityUtils;
import org.vadim.config.security.port.SecurityUtilsImpl;
import org.vadim.dto.AccountCredentialsDto;
import org.vadim.entity.Account;

public class SecurityUtilsTest {

    private final SecurityUtils securityUtils = new SecurityUtilsImpl();

    private final JwtService jwtService = new JwtServiceImpl("sleqgrbwery3v45oq347qo3748vtq34qg7424794c0n3491m34138n4t328", 1);

    private final AccountCredentialsDto testCredetials = new AccountCredentialsDto(
            "USER",
            "user@example.com",
            "@User",
            "user123"
    );

    @AfterEach
    void clearContext(){
        SecurityContextHolder.clearContext();
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "100500"
    })
    void test(Long userId){
        Account account = new Account();
        account.setId(userId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                account, null, null
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        jwtService.generateToken(userId.toString());
        Assertions.assertEquals(userId, securityUtils.getAccountIdFromToken());
    }
}
