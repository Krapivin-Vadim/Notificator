package org.vadim.controller.port;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.vadim.dto.RemindCreateDto;

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
    public ResponseEntity<Void> createNewRemind(@Validated(value = RemindCreateDto.RemindGroupSequenceValidation.class)
                                                @RequestBody RemindCreateDto reminderCreateDto);
}
