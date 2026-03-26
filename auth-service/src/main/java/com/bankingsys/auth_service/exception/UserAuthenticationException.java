package com.bankingsys.auth_service.exception;

public class UserAuthenticationException extends RuntimeException{
    public UserAuthenticationException(String msg){
        super(msg);
    }
}
