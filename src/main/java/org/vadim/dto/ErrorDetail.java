package org.vadim.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorDetail(
        @Schema(
                description = "Поле, спровоцировавшее ошибку",
                example = "username"
        )
        String field,

        @Schema(
                description = "Сообщение об ошибке",
                example = "Не должно быть пустым"
        )
        String msg
) {
}
