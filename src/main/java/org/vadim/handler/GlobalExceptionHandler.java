package org.vadim.handler;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.vadim.dto.ErrorDetail;
import org.vadim.dto.ErrorResponseDto;
import org.vadim.exception.NotificationServiceException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotificationServiceException.class)
    public ResponseEntity<ErrorResponseDto> handleNotificationServiceException(NotificationServiceException e){
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), e.getHttpStatus());
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ErrorDetail> errorDetails = ex.getBindingResult().getAllErrors().stream()
                .map(objectError -> (FieldError) objectError)
                .map(fieldError -> new ErrorDetail(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                )).toList();
        return new ResponseEntity<>(new ErrorResponseDto("Validation failed", errorDetails), status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e){
        log.error("Unexpected error: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto("Unexpected exception has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
