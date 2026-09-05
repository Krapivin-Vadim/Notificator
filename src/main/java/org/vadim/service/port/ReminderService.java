package org.vadim.service.port;

import org.vadim.dto.RemindCreateDto;
import org.vadim.dto.RemindCreationResponseDto;

public interface ReminderService {
    RemindCreationResponseDto createRemind(RemindCreateDto remindCreateDto);
}
