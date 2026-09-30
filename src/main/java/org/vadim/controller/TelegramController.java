package org.vadim.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vadim.config.security.port.SecurityUtils;
import org.vadim.dto.TelegramConnectDto;
import redis.clients.jedis.RedisClient;

import java.util.UUID;

@RestController
@RequestMapping("telegram")
@SecurityRequirement(name = "bearerAuth")
public class TelegramController {
    private final String REDIS_SET_KEY;
    private final Long REDIS_TTL;

    private final String BOT_NAME;
    private final SecurityUtils securityUtils;
    private final RedisClient redisClient;

    @GetMapping("/connect")
    TelegramConnectDto connectTelegram(){
        String userId = securityUtils.getAccountIdFromToken().toString();
        UUID telegramAuthToken = UUID.randomUUID();
        redisClient.hset(REDIS_SET_KEY, telegramAuthToken.toString(), userId);
        redisClient.hexpire(REDIS_SET_KEY, REDIS_TTL, telegramAuthToken.toString());
        return new TelegramConnectDto(BOT_NAME, telegramAuthToken);
    }

    @Autowired
    public TelegramController(@Value("${telegram.bot.name}") String botName,
                              @Value("${redis.auth.set-key}") String redisSetKey,
                              @Value("${redis.auth.ttl}") Long ttl,
                              SecurityUtils securityUtils,
                              RedisClient redisClient) {
        this.BOT_NAME = botName;
        this.securityUtils = securityUtils;
        this.redisClient = redisClient;
        REDIS_SET_KEY = redisSetKey;
        REDIS_TTL = ttl;
    }
}
