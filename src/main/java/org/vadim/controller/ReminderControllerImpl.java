package org.vadim.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vadim.controller.port.ReminderController;
import org.vadim.dto.RemindCreateDto;
import org.vadim.repository.ReminderRepository;
import org.vadim.service.EmailNotificationService;
import org.vadim.service.port.ReminderService;

import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("remind")
@SecurityRequirement(name = "bearerAuth")
public class ReminderControllerImpl implements ReminderController {
    private final ReminderService reminderService;

    @PutMapping("/create")
    public ResponseEntity<Void> createNewRemind(@Validated(value = RemindCreateDto.RemindGroupSequenceValidation.class)
                                                    @RequestBody RemindCreateDto reminderCreateDto){
        reminderService.createRemind(reminderCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> deleteRemind(Long remindId) {
        try {
            reminderService.deleteRemind(remindId);
        } catch (SchedulerException e) {
            log.error("Error during deleting quartz jobs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Тестовая ручка. Потом надо удалить
     * @return
     */
    @PutMapping("/test")
    public ResponseEntity<Void> createTestRemind(){
        reminderService.createRemind(new RemindCreateDto(
                "My title",
                "My description",
                OffsetDateTime.now().plusMinutes(1)
        ));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
