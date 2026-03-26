package com.wolfgang.showcase.appointment.web;

import com.wolfgang.showcase.appointment.dto.ErrorResponse;
import com.wolfgang.showcase.appointment.dto.ValidationViolation;
import com.wolfgang.showcase.appointment.service.AppointmentConflictException;
import com.wolfgang.showcase.appointment.service.AppointmentNotFoundException;
import com.wolfgang.showcase.appointment.service.InvalidAppointmentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<ValidationViolation> violations = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toViolation)
                .collect(Collectors.toList());

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Request validation failed.",
                request.getRequestURI(),
                violations
        );
    }

    @ExceptionHandler(InvalidAppointmentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAppointment(
            InvalidAppointmentException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI(),
                Collections.emptyList()
        );
    }

    @ExceptionHandler(AppointmentConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            AppointmentConflictException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request.getRequestURI(),
                Collections.emptyList()
        );
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            AppointmentNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI(),
                Collections.emptyList()
        );
    }

    private ValidationViolation toViolation(FieldError fieldError) {
        return new ValidationViolation(fieldError.getField(), fieldError.getDefaultMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            String path,
            List<ValidationViolation> violations
    ) {
        ErrorResponse response = new ErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                violations
        );
        return ResponseEntity.status(status).body(response);
    }
}
