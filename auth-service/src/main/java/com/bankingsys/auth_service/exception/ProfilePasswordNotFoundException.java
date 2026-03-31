package com.bankingsys.auth_service.exception;

public class ProfilePasswordNotFoundException extends RuntimeException{
    public ProfilePasswordNotFoundException(String messsage){
        super(messsage);
    }
}
