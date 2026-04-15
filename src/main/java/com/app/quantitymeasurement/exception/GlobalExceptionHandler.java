package com.app.quantitymeasurement.exception;

import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "Quantity Measurement Error";

    // Handle @Valid validation errors (request body)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        log.warn("Validation error: {}", message);

        return ResponseEntity.badRequest().body(
                buildError(HttpStatus.BAD_REQUEST.value(), ERROR, message, request.getRequestURI())
        );
    }

    // Handle @RequestParam / @PathVariable validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraint(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        log.warn("Constraint error: {}", message);

        return ResponseEntity.badRequest().body(
                buildError(HttpStatus.BAD_REQUEST.value(), ERROR, message, request.getRequestURI())
        );
    }

    // Handle custom business exception
    @ExceptionHandler(QuantityMeasurementException.class)
    public ResponseEntity<Map<String, Object>> handleQuantityException(
            QuantityMeasurementException ex,
            HttpServletRequest request) {

        log.warn("Business error: {}", ex.getMessage());

        return ResponseEntity.badRequest().body(
                buildError(HttpStatus.BAD_REQUEST.value(), ERROR, ex.getMessage(), request.getRequestURI())
        );
    }

    // Handle invalid arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("Invalid argument: {}", ex.getMessage());

        return ResponseEntity.badRequest().body(
                buildError(HttpStatus.BAD_REQUEST.value(), ERROR, ex.getMessage(), request.getRequestURI())
        );
    }

    // Handle ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(
            ResponseStatusException ex,
            HttpServletRequest request) {

        log.warn("ResponseStatusException: {}", ex.getReason());

        return ResponseEntity.status(ex.getStatusCode()).body(
                buildError(ex.getStatusCode().value(), ex.getReason(), ex.getReason(), request.getRequestURI())
        );
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobal(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unhandled error: {}", ex.getMessage());

        return ResponseEntity.internalServerError().body(
                buildError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        ex.getMessage(),
                        request.getRequestURI())
        );
    }

    // Build standard error response
    private Map<String, Object> buildError(int status, String error,
                                           String message, String path) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return body;
    }
}