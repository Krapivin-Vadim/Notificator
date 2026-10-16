package org.vadim.service.port;

import org.quartz.SchedulerException;
import org.vadim.dto.RemindCreateDto;

public interface ReminderService {
    void createRemind(RemindCreateDto remindCreateDto);
    void deleteRemind(Long remindId) throws SchedulerException;
}
