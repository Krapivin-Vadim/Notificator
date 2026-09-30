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

@Slf4j
@RequiredArgsConstructor
public class TelegramRequestExecutor{

    private final AccountRepository accountRepository;
    private final TelegramClient client;
    private final Update update;
    private static final String MSG = "Your telegram account successfully connected";
    private static final String ERROR_MSG = "Unable to connect your telegram account";

    public void execute() {
        if (!update.hasMessage() || !update.getMessage().hasText()){
            return;
        }
        var message = update.getMessage().getText();
        if(!message.startsWith("/start")){
            return;
        }
        var chatId = update.getMessage().getChatId();
        //TODO: Добавь извлечение userId из payload
        Long userId = 1L;
        var accountOptional = accountRepository.findById(userId);
        if(accountOptional.isEmpty()){
            log.warn("Cannot bind user with id {} to telegram", userId);
            sendResponse(ERROR_MSG, chatId);
            return;
        }
        Account acc = accountOptional.get();
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
