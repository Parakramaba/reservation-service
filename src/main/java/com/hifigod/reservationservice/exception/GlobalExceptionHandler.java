package com.hifigod.reservationservice.exception;

import com.hifigod.reservationservice.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Response errorDetails = new Response(HttpStatus.NOT_FOUND.value(),
                request.getDescription(false),
                ex.getMessage(),
                LocalDateTime.now(),
                null);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
