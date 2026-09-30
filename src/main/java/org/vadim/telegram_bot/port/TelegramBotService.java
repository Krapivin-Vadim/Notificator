package org.vadim.telegram_bot.port;

import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vadim.entity.Account;

public interface TelegramBotService extends LongPollingUpdateConsumer, SpringLongPollingBot {
    void sendMessage(String message, Account account) throws TelegramApiException;
}
