package org.vadim.service.port;

import org.vadim.entity.Reminder;

public interface NotificationService {
    void sendNotification(Reminder reminder);
}
