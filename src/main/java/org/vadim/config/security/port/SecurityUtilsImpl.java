package org.vadim.config.security.port;

import org.springframework.stereotype.Component;

@Component
public class SecurityUtilsImpl implements SecurityUtils{
    @Override
    public Long getAccountIdFromToken() {
        return 1L;
    }
}
