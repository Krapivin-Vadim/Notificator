package org.vadim.Job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vadim.Job.port.NotificationJob;
import org.vadim.repository.ReminderRepository;
import org.vadim.service.port.NotificationService;

@Component
public class TelegramNotificationJob extends NotificationJob {

    private final String PREFIX = "telegram";

    public TelegramNotificationJob(@Qualifier("telegramNotificationService") NotificationService notificationService,
                                   ReminderRepository reminderRepository) {
        super(notificationService, reminderRepository);
    }
}
