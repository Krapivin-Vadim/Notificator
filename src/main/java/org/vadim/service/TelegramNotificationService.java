package org.vadim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vadim.entity.Reminder;
import org.vadim.exception.TelegramClientException;
import org.vadim.service.port.NotificationService;
import org.vadim.telegram_bot.TelegramBotServiceImpl;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {

    private static final String MSG_TEMPLATE = "%s \n %s";

    private final TelegramBotServiceImpl telegramBot;

    @Override
    public void sendNotification(Reminder reminder) {
        String title = reminder.getTitle();
        String description = reminder.getDescription();
        try {
            telegramBot.sendMessage(MSG_TEMPLATE.formatted(title, description), reminder.getAccount());
        } catch (TelegramApiException e) {
            log.warn("Unable to send notification {}", reminder.getId());
            throw new TelegramClientException();
        }
    }
}
