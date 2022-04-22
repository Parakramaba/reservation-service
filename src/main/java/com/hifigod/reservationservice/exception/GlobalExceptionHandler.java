package com.hifigod.reservationservice.exception;

import com.google.zxing.WriterException;
import com.hifigod.reservationservice.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
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

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex, WebRequest request) {
        Response errorDetails = new Response(HttpStatus.BAD_REQUEST.value(),
                request.getDescription(false),
                ex.getMessage(),
                LocalDateTime.now(),
                null);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex, WebRequest request) {
        Response errorDetails = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false),
                ex.getMessage(),
                LocalDateTime.now(),
                null);

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WriterException.class)
    public ResponseEntity<?> handleWriterException(WriterException ex, WebRequest request) {
        Response errorDetails = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getDescription(false),
                ex.getMessage(),
                LocalDateTime.now(),
                null);

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
