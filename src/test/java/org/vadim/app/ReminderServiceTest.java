package org.vadim.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.vadim.config.security.port.SecurityUtils;
import org.vadim.dto.RemindCreateDto;
import org.vadim.entity.Account;
import org.vadim.entity.Reminder;
import org.vadim.exception.NotEnoughRightsException;
import org.vadim.exception.RemindCreationException;
import org.vadim.exception.RemindNotFoundException;
import org.vadim.repository.AccountRepository;
import org.vadim.repository.ReminderRepository;
import org.vadim.service.ReminderServiceImpl;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceTest {

    private Account testAccount;

    private Reminder testReminder;

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private ReminderServiceImpl reminderService;

    @BeforeEach
    void init() throws SchedulerException {
        Mockito.lenient().when(scheduler.deleteJob(Mockito.any())).thenReturn(true);

        testAccount = new Account(
                1L,
                "username",
                "email",
                1L,
                "passwd",
                null
        );

        testReminder = new Reminder(
                1L,
                testAccount,
                "Title",
                "Description",
                Instant.now()
        );
    }

    @Test
    void deleteRemindSuccess() throws SchedulerException {
        Mockito.when(securityUtils.getAccountIdFromToken()).thenReturn(testAccount.getId());
        Mockito.when(reminderRepository.findById(Mockito.any())).thenReturn(Optional.of(testReminder));
        reminderService.deleteRemind(testReminder.getId());
    }

    @Test
    void deleteSomeoneElseRemind(){
        Mockito.when(securityUtils.getAccountIdFromToken()).thenReturn(testAccount.getId() + 1);
        Mockito.when(reminderRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testReminder));
        Assertions.assertThrows(NotEnoughRightsException.class, () -> reminderService.deleteRemind(testReminder.getId()));
    }

    @Test
    void deleteForNotExistingRemind(){
        Mockito.when(securityUtils.getAccountIdFromToken()).thenReturn(testAccount.getId());
        Mockito.when(reminderRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(RemindNotFoundException.class, () -> reminderService.deleteRemind(testReminder.getId()));
    }

    @Test
    void createRemindSuccess(){
        Mockito.when(securityUtils.getAccountIdFromToken()).thenReturn(testAccount.getId());
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testAccount));
        Mockito.when(reminderRepository.save(Mockito.any())).then((invocation -> {
            Reminder reminder = invocation.getArgument(0);
            reminder.setId(1L);
            return reminder;
        }));
        reminderService.createRemind(new RemindCreateDto(
                testReminder.getTitle(),
                testReminder.getDescription(),
                OffsetDateTime.now()
        ));
    }

    @Test
    void createRemindError() throws SchedulerException {
        Mockito.when(securityUtils.getAccountIdFromToken()).thenReturn(testAccount.getId());
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testAccount));
        Mockito.when(reminderRepository.save(Mockito.any())).then((invocation -> {
            Reminder reminder = invocation.getArgument(0);
            reminder.setId(1L);
            return reminder;
        }));
        Mockito.when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(testAccount));
        Mockito.when(scheduler.scheduleJob(Mockito.any(), Mockito.any())).thenThrow(SchedulerException.class);
        Assertions.assertThrows(RemindCreationException.class, () -> reminderService.createRemind(new RemindCreateDto(
                testReminder.getTitle(),
                testReminder.getDescription(),
                OffsetDateTime.now()
        )));
    }
}