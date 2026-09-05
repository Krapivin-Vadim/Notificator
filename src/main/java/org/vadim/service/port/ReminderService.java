package org.vadim.service.port;

import org.vadim.dto.RemindCreateDto;

public interface ReminderService {
    void createRemind(RemindCreateDto remindCreateDto);
}
