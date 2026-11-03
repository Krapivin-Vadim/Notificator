package org.vadim.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReminderSortField {
    NOTIFY_TIME("notifyTime"),
    TITLE("title");

    @Getter
    private final String property;
}
