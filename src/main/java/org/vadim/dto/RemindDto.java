package org.vadim.dto;

import org.vadim.entity.Reminder;

import java.time.Instant;

public record RemindDto(
        String title,
        String text,
        Instant notifyAt
) {
    public static RemindDto from(Reminder reminder){
        return new RemindDto(
                reminder.getTitle(),
                reminder.getDescription(),
                reminder.getNotifyTime());
    }
}
