package org.vadim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.vadim.entity.Reminder;
import org.vadim.service.port.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(Reminder reminder) {
        log.info("Starting email sending for remind {}", reminder.getId());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(reminder.getAccount().getEmail());
        message.setSubject(reminder.getTitle());
        message.setText(reminder.getDescription());
        message.setFrom("krapiwinvadim@yandex.ru");
        mailSender.send(message);
        log.info("Remind {} was sent", reminder.getId());
    }
}
