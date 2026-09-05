package org.vadim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vadim.Job.EmailNotificationJob;
import org.vadim.Job.NotificationType;
import org.vadim.Job.TelegramNotificationJob;
import org.vadim.Job.port.NotificationJob;
import org.vadim.config.security.port.SecurityUtils;
import org.vadim.dto.RemindCreateDto;
import org.vadim.dto.RemindCreationResponseDto;
import org.vadim.dto.RemindCreationStatus;
import org.vadim.entity.Account;
import org.vadim.entity.Reminder;
import org.vadim.exception.AccountNotFoundException;
import org.vadim.repository.AccountRepository;
import org.vadim.repository.ReminderRepository;
import org.vadim.service.port.ReminderService;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {
    private final String REMIND_ID_KEY = "remindId";

    private final AccountRepository accountRepository;
    private final SecurityUtils securityUtils;
    private final ReminderRepository reminderRepository;
    private final Scheduler scheduler;

    @Override
    @Transactional
    public RemindCreationResponseDto createRemind(RemindCreateDto remindCreateDto) {
        //TODO: Добавь функционал извлечения id пользователя из JWT
        Long accountId = securityUtils.getAccountIdFromToken();
        Account acc = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        log.info("User with id={} creates new remind", accountId);
        Reminder reminder = new Reminder();
        reminder.setAccount(acc);
        reminder.setTitle(remindCreateDto.title());
        reminder.setDescription(remindCreateDto.description());
        reminder.setNotifyTime(remindCreateDto.remindAt().toInstant());
        log.info("Saving new remind for user {}", accountId);
        reminderRepository.save(reminder);

        var email = createJob(reminder, EmailNotificationJob.class, NotificationType.EMAIL);
        var telegram = acc.getTelegram() != null ?
                createJob(reminder, TelegramNotificationJob.class, NotificationType.TELEGRAM) :
                RemindCreationStatus.FAILED;

        if(email.equals(telegram) && email.equals(RemindCreationStatus.FAILED)){
            reminderRepository.delete(reminder);
        }
        return new RemindCreationResponseDto(email, telegram);
    }

    private RemindCreationStatus createJob(Reminder reminder,
                           Class<? extends NotificationJob> notificationJobClass,
                           NotificationType notificationType) {
        log.info("Creating new {} notification job for remind {}", notificationType, reminder.getId());
        JobKey jobKey = new JobKey(
                reminder.getId().toString(),
                reminder.getAccount().getId()+ "_" + notificationType);

        TriggerKey triggerKey = new TriggerKey(
                reminder.getId().toString(),
                reminder.getAccount().getId() + "_" + notificationType);

        JobDetail jobDetail = JobBuilder.newJob(notificationJobClass)
                .withIdentity(jobKey)
                .usingJobData(REMIND_ID_KEY, reminder.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .forJob(jobKey)
                .startAt(reminder.getNotifyTime())
                .build();
        log.info("Scheduling new {} notification job for remind {}", notificationType, reminder.getId());
        try{
            scheduler.scheduleJob(jobDetail, trigger);
            return RemindCreationStatus.SUCCESS;
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return RemindCreationStatus.FAILED;
        }
    }
}
