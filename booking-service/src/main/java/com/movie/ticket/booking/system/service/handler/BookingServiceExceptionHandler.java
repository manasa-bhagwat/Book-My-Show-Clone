package com.movie.ticket.booking.system.service.handler;

import com.movie.ticket.booking.system.service.dto.ResponseDTO;
import com.movie.ticket.booking.system.service.exception.BookingsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class BookingServiceExceptionHandler {

    private static final Map<HttpStatus, String> STATUS_CODE_DESCRIPTIONS = Map.of(
            HttpStatus.BAD_REQUEST, "Invalid request. Please check the provided data.",
            HttpStatus.NOT_FOUND, "The requested resource was not found.",
            HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred on the server.",
            HttpStatus.UNAUTHORIZED, "You are not authorized to perform this action.",
            HttpStatus.FORBIDDEN, "Access to this resource is forbidden."
    );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(MethodArgumentNotValidException exception) {

        log.warn("Validation error occurred: {}", exception.getMessage());

        // Extract field errors
        List<String> errorMessages = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> "Field [" + error.getField() + "] : " + error.getDefaultMessage())
                .collect(Collectors.toList());

        // Extract global errors (if any)
        errorMessages.addAll(exception.getBindingResult().getGlobalErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList());

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(
                ResponseDTO.builder()
                        .errorCode(status.value())  // Numeric status code
                        .errorStatusCodeDescription(STATUS_CODE_DESCRIPTIONS.get(status))  // Readable description
                        .errorMessages(errorMessages)
                        .build()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseDTO> handleNullPointerException(NullPointerException exception) {
        log.error("NullPointerException occurred: {}", exception.getMessage(), exception);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status).body(
                ResponseDTO.builder()
                        .errorCode(status.value())
                        .errorStatusCodeDescription(STATUS_CODE_DESCRIPTIONS.get(status))
                        .errorMessages(List.of("A required value was missing or null."))
                        .build()
        );
    }

    @ExceptionHandler(BookingsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleBookingsException(BookingsException exception) {
        log.error("BookingsException occurred: {}", exception.getMessage(), exception);

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(
                ResponseDTO.builder()
                        .errorCode(status.value())
                        .errorStatusCodeDescription(STATUS_CODE_DESCRIPTIONS.get(status))
                        .errorMessages(List.of(exception.getMessage()))
                        .build()
        );
    }



}
