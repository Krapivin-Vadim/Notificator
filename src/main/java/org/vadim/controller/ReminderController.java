package org.vadim.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vadim.dto.RemindCreateDto;
import org.vadim.repository.ReminderRepository;
import org.vadim.service.EmailNotificationService;
import org.vadim.service.port.ReminderService;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("remind")
@SecurityRequirement(name = "bearerAuth")
public class ReminderController {
    private final ReminderService reminderService;

    private final EmailNotificationService notificationService;
    private final ReminderRepository reminderRepository;

    //TODO: Доработай преобразование OffsetDateTime в Instant
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешное создание попытки"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Пользователь не авторизован"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка создания нотификаций / неизвестная ошибка"
            )
    })
    @PutMapping("/create")
    public ResponseEntity<Void> createNewRemind(@Validated(value = RemindCreateDto.RemindGroupSequenceValidation.class)
                                                    @RequestBody RemindCreateDto reminderCreateDto){
        reminderService.createRemind(reminderCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
