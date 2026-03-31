package com.bankingsys.auth_service.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<String> handleInvalidFormat(EntityExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        Map<String, String> error = Map.of(
                "error", "Invalid input format",
                "message", "follow yyyy-MM-dd"
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(CustomerServiceException.class)
    public ResponseEntity<String> handleCustomerServiceException(CustomerServiceException ex){
        return ResponseEntity
                .status(409)
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<String> handleCustomerServiceException(UserAuthenticationException ex){
        return ResponseEntity
                .status(401)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ProfilePasswordNotFoundException.class)
    public ResponseEntity<HttpStatus> handlePasswordNotFound(ProfilePasswordNotFoundException ex){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VerificationFailedException.class)
    public ResponseEntity<HttpStatus> handleVerificationException(VerificationFailedException ex){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
