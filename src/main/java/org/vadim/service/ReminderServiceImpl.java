package org.vadim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vadim.Job.EmailNotificationJob;
import org.vadim.Job.NotificationType;
import org.vadim.Job.TelegramNotificationJob;
import org.vadim.Job.port.NotificationJob;
import org.vadim.config.security.port.SecurityUtils;
import org.vadim.dto.*;
import org.vadim.entity.Account;
import org.vadim.entity.Reminder;
import org.vadim.exception.AccountNotFoundException;
import org.vadim.exception.NotEnoughRightsException;
import org.vadim.exception.RemindCreationException;
import org.vadim.exception.RemindNotFoundException;
import org.vadim.repository.AccountRepository;
import org.vadim.repository.ReminderRepository;
import org.vadim.service.port.ReminderService;
import org.vadim.spec.ReminderSpecification;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {
    private final String REMIND_ID_KEY = "remindId";
    private final int MAX_PAGE_SIZE = 20;

    private final AccountRepository accountRepository;
    private final SecurityUtils securityUtils;
    private final ReminderRepository reminderRepository;
    private final Scheduler scheduler;

    @Override
    public Page<RemindDto> search(ReminderFilter filter, int page, int size) {
        Long accountId = securityUtils.getAccountIdFromToken();

        Specification<Reminder> specification = Specification
                .where(ReminderSpecification.belongTo(accountId))
                .and(ReminderSpecification.titleContains(filter.title()))
                .and(ReminderSpecification.notifyAfter(filter.after()))
                .and(ReminderSpecification.notifyBefore(filter.before()));

        ReminderSortField sortField = filter.sortBy() == null
                ? ReminderSortField.NOTIFY_TIME
                : filter.sortBy();

        Sort.Direction sortDirection = filter.sortDirection() == null
                ? Sort.Direction.DESC
                : filter.sortDirection();

        Sort sort = Sort.by(sortDirection, sortField.getProperty());
        Pageable pageable = PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE), sort);
        return reminderRepository.findAll(specification, pageable).map(RemindDto::from);
    }

    @Override
    @Transactional
    public void createRemind(RemindCreateDto remindCreateDto) {
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
        log.debug("New remind created {}", reminder);

        var email = createJob(reminder, EmailNotificationJob.class, NotificationType.EMAIL);
        var telegram = acc.getTelegram() != null ?
                createJob(reminder, TelegramNotificationJob.class, NotificationType.TELEGRAM) :
                RemindCreationStatus.FAILED;

        if(email.equals(telegram) && email.equals(RemindCreationStatus.FAILED)){
            reminderRepository.delete(reminder);
            log.debug("Delete new remind due to Quartz scheduler error {}", reminder);
            throw new RemindCreationException(reminder.getId());
        }
    }

    @Override
    @Transactional
    public void deleteRemind(Long remindId) throws SchedulerException {
        Long userId = securityUtils.getAccountIdFromToken();
        Reminder reminder = reminderRepository.findById(remindId).orElseThrow(() -> new RemindNotFoundException(remindId));
        if (!reminder.getAccount().getId().equals(userId)){
            throw new NotEnoughRightsException();
        }
        reminderRepository.delete(reminder);
        List<JobKey> jobKeys = List.of(
                buildJobKey(userId, remindId, NotificationType.EMAIL),
                buildJobKey(userId, remindId, NotificationType.TELEGRAM)
        );
        for (JobKey jobKey : jobKeys) {
            boolean res = scheduler.deleteJob(jobKey);
            if (!res) {
                log.warn("Fail during removing job {}", jobKey);
                continue;
            }
            log.info("Successfully deleted job {}", jobKey);
        }
        log.info("Remind with id {} was deleted", remindId);
    }

    private RemindCreationStatus createJob(Reminder reminder,
                           Class<? extends NotificationJob> notificationJobClass,
                           NotificationType notificationType) {
        log.info("Creating new {} notification job for remind {}", notificationType, reminder.getId());
        JobKey jobKey = buildJobKey(reminder.getAccount().getId(), reminder.getId(), notificationType);

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

    private JobKey buildJobKey(Long userId, Long remindId, NotificationType type){
        return JobKey.jobKey(remindId.toString(), userId.toString() + "_" + type);
    }
}
