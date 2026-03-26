package com.bankingsys.customer_service.exceptionhandler;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String message){
        super(message);
    }
}
