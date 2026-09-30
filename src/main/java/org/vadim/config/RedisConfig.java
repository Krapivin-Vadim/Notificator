package org.vadim.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.*;

@Configuration
public class RedisConfig {

    private final HostAndPort hostAndPort;
    //private final JedisClientConfig redisConfig;
    private final ConnectionPoolConfig poolConfig;

    public RedisConfig(@Value("${redis.host}") String host,
                       @Value("${redis.port}") Integer port,
                       @Value("${redis.user}") String user,
                       @Value("${redis.password}") String password,
                       @Value("${redis.num-connections}")Integer numConnections) {
        hostAndPort = new HostAndPort(host, port);
//        redisConfig = DefaultJedisClientConfig.builder()
//                .user(user)
//                .password(password)
//                .build();
        poolConfig = new ConnectionPoolConfig();
        poolConfig.setMaxTotal(numConnections);
    }

    @Bean
    RedisClient getRedisConnectionPool(){
        return new RedisClient.Builder()
                .hostAndPort(hostAndPort)
                .poolConfig(poolConfig)
                //.clientConfig(redisConfig)
                .build();
    }
}
