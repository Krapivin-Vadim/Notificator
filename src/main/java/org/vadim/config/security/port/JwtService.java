package org.vadim.config.security.port;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserId(String token);
    String generateToken(String accountId);
}
