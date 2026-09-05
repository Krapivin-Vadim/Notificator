package org.vadim.Job.port;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.vadim.exception.RemindNotFounfException;
import org.vadim.repository.ReminderRepository;
import org.vadim.service.port.NotificationService;

@Slf4j
@AllArgsConstructor
public abstract class NotificationJob extends QuartzJobBean {
    private final String REMIND_ID_KEY = "remindId";

    protected final NotificationService notificationService;
    private final ReminderRepository reminderRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Long remindId = context.getJobDetail().getJobDataMap().getLong(REMIND_ID_KEY);
        log.info("Processing remind {}", remindId);
        var reminder = reminderRepository.findById(remindId).orElseThrow(() -> new RemindNotFounfException(remindId));
        notificationService.sendNotification(reminder);
        log.info("Remind {} was processed", remindId);
    }
}
