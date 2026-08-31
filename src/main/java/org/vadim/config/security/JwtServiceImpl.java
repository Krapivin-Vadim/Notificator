package org.vadim.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.vadim.config.security.port.JwtService;
import org.vadim.exception.InvalidAccessTokenException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;
    private final long jwtExpMs;

    JwtServiceImpl(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.exp-seconds}") long jwtExpSeconds
    ){
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.jwtExpMs = jwtExpSeconds * 1000;
    }


    @Override
    public String extractUserId(String token) {
        String accountId = getClaims(token).getSubject();
        if(!StringUtils.hasText(accountId)){
            String message = "JWT does not contains accountId";
            log.warn(message);
            throw new InvalidAccessTokenException(message);
        }
        return accountId;
    }

    @Override
    public String generateToken(String accountId) {
        Date issuedAt = new Date();
        Date expAt = new Date(issuedAt.getTime() + jwtExpMs);
        return Jwts.builder()
                .claims(Map.of())
                .subject(accountId)
                .issuedAt(issuedAt)
                .expiration(expAt)
                .signWith(secretKey)
                .compact();
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
