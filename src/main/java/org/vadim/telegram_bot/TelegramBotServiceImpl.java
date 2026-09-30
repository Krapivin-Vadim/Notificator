package org.vadim.telegram_bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.vadim.entity.Account;
import org.vadim.repository.AccountRepository;
import org.vadim.telegram_bot.port.TelegramBotService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TelegramBotServiceImpl implements TelegramBotService {

    private final AccountRepository accountRepository;
    private final TelegramClient telegramClient;
    private final ExecutorService threadPool;
    private final String botToken;

    @Override
    public void consume(List<Update> updates) {
        for(Update update : updates){
            TelegramRequestExecutor executor = new TelegramRequestExecutor(
                    accountRepository,
                    telegramClient,
                    update);
            threadPool.execute(executor::execute);
        }
    }

    @Override
    public void sendMessage(String message, Account account) throws TelegramApiException {
        SendMessage sendMessage = SendMessage.builder()
                .text(message)
                .chatId(account.getTelegram())
                .build();
        telegramClient.execute(sendMessage);
    }

    @Autowired
    public TelegramBotServiceImpl(AccountRepository accountRepository,
                                  TelegramClient telegramClient,
                                  @Value("${telegram.threads}") Integer numThreads,
                                  @Value("${telegram.bot.token}") String botToken) {
        this.accountRepository = accountRepository;
        this.telegramClient = telegramClient;
        threadPool = Executors.newFixedThreadPool(numThreads);
        this.botToken = botToken;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
}
