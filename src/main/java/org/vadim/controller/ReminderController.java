package org.vadim.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vadim.dto.RemindCreateDto;
import org.vadim.dto.RemindCreationResponseDto;
import org.vadim.repository.ReminderRepository;
import org.vadim.service.EmailNotificationService;
import org.vadim.service.port.ReminderService;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@RequiredArgsConstructor
@RestController
@RequestMapping("remind")
@SecurityRequirement(name = "bearerAuth")
public class ReminderController {
    private final ReminderService reminderService;

    private final EmailNotificationService notificationService;
    private final ReminderRepository reminderRepository;

    @PutMapping("/create")
    public ResponseEntity<RemindCreationResponseDto> createNewRemind(@Validated(value = RemindCreateDto.RemindGroupSequenceValidation.class)
                                                    @RequestBody RemindCreateDto reminderCreateDto){
        return ResponseEntity.ok(reminderService.createRemind(reminderCreateDto));
    }

    @PutMapping("/test")
    public ResponseEntity<RemindCreationResponseDto> createTestRemind(){
        var response = reminderService.createRemind(new RemindCreateDto(
                "My title",
                "My description",
                OffsetDateTime.now().plusMinutes(1)
        ));
        return ResponseEntity.ok(response);
    }
}
