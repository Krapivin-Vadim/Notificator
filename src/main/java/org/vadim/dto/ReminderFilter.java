package org.vadim.dto;

import org.springframework.data.domain.Sort;

import java.time.Instant;

public record ReminderFilter(
        String title,
        Instant after,
        Instant before,
        ReminderSortField sortBy,
        Sort.Direction sortDirection
) {
}
