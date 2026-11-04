package org.vadim.service.port;

import org.quartz.SchedulerException;
import org.springframework.data.domain.Page;
import org.vadim.dto.RemindCreateDto;
import org.vadim.dto.RemindDto;
import org.vadim.dto.ReminderFilter;
import org.vadim.entity.Reminder;

public interface ReminderService {
    void createRemind(RemindCreateDto remindCreateDto);
    void deleteRemind(Long remindId) throws SchedulerException;
    Page<RemindDto> search(ReminderFilter reminderFilter, int page, int size);
}
