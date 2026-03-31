package com.bankingsys.account_service.exception;

public class AuthServiceException extends RuntimeException{
    public AuthServiceException(String message){
        super(message);
    }
}
