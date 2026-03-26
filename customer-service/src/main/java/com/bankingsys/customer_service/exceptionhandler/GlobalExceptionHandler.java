package com.bankingsys.customer_service.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<?> handle(CustomerAlreadyExistsException ex){
        return ResponseEntity
                .status(409)
                .body(ex.getMessage());
    }
}
