package org.vadim.controller.port;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vadim.dto.RemindCreateDto;
import org.vadim.dto.RemindDto;
import org.vadim.dto.ReminderFilter;

public interface ReminderController {
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
    ResponseEntity<Void> createNewRemind(@Validated(value = RemindCreateDto.RemindGroupSequenceValidation.class)
                                                @RequestBody RemindCreateDto reminderCreateDto);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удаление напоминания прошло успешно"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "У пользователя нет прав на удаление напоминаия"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка Quartz Scheduler/неизвестная ошибка"
            )
    })
    @DeleteMapping("/{remindId}")
    ResponseEntity<Void> deleteRemind(@PathVariable @Min(1) Long remindId);

    @GetMapping("/list")
    Page<RemindDto> getRemindList(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                  @RequestParam(defaultValue = "20") @Min(1) Integer size,
                                  @ModelAttribute ReminderFilter filter);
}
