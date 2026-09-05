package org.vadim.service;

import org.springframework.stereotype.Service;
import org.vadim.entity.Reminder;
import org.vadim.service.port.NotificationService;

@Service
public class TelegramNotificationService implements NotificationService {
    @Override
    public void sendNotification(Reminder reminder) {
        System.out.println(reminder.getDescription());
    }
}
