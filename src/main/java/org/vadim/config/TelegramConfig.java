package org.vadim.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfig {

    private final String TOKEN;

    public TelegramConfig(@Value("${telegram.bot.token}") String token) {
        this.TOKEN = token;
    }

    @Bean
    TelegramClient getBot(){
        return new OkHttpTelegramClient(TOKEN);
    }
}
