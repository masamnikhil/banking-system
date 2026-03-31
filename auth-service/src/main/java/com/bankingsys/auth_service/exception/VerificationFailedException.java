package com.bankingsys.auth_service.exception;

public class VerificationFailedException extends RuntimeException{

    public VerificationFailedException(String message) {
        super(message);
    }
}
