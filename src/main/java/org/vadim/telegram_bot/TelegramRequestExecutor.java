package org.vadim.telegram_bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.vadim.entity.Account;
import org.vadim.exception.TelegramClientException;
import org.vadim.repository.AccountRepository;
import redis.clients.jedis.RedisClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class TelegramRequestExecutor{

    private final AccountRepository accountRepository;
    private final RedisClient redisClient;
    private final String redisSetKey;
    private final TelegramClient client;
    private final Update update;
    private static final String MSG = "Your telegram account successfully connected";
    private static final String ERROR_MSG = "Unable to connect your telegram account";

    public void execute() {
        if (!update.hasMessage() || !update.getMessage().hasText()){
            return;
        }
        var chatId = update.getMessage().getChatId();
        var message = update.getMessage().getText();
        if(message.startsWith("/start")){
            start(chatId);
            return;
        }
        if(message.startsWith("/auth")){
            auth(message, chatId);
            return;
        }
        sendResponse("No such command", chatId);
    }

    private void start( Long chatId){
        sendResponse("Paste authentication token to get telegram notifications", chatId);
    }

    private void auth(String message, Long chatId){
        List<String> parsedMessage = Arrays.stream(message.split(" ")).toList();
        UUID key;
        try {
            key = UUID.fromString(parsedMessage.get(1));
        } catch (IllegalArgumentException e){
            sendResponse("Invalid command", chatId);
            return;
        }
        String userIdStr = redisClient.hget(redisSetKey, key.toString());
        Optional<Account> accountOptional;
        try {
            accountOptional = accountRepository.findById(Long.parseLong(userIdStr));
        }
        catch (RuntimeException e){
            sendResponse("Telegram authentication token is expired or not valid", chatId);
            return;
        }
        if (accountOptional.isEmpty()){
            sendResponse("No user for this authentication token", chatId);
            return;
        }
        var acc = accountOptional.get();
        acc.setTelegram(chatId);
        accountRepository.save(acc);
        sendResponse(MSG, chatId);
    }

    private void sendResponse(String textMessage, Long chatId){
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(textMessage)
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("Unable to response to chat {}", chatId);
            e.printStackTrace();
            throw new TelegramClientException();
        }
    }
}
