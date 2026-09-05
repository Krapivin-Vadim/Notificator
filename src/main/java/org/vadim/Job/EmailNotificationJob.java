package org.vadim.Job;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vadim.Job.port.NotificationJob;
import org.vadim.repository.ReminderRepository;
import org.vadim.service.port.NotificationService;

@Component
public class EmailNotificationJob extends NotificationJob {
    private final String PREFIX = "email";

    public EmailNotificationJob(@Qualifier("emailNotificationService") NotificationService notificationService,
                                ReminderRepository reminderRepository) {
        super(notificationService, reminderRepository);
    }
}
