package com.project.ecomapplication.exceptions;

public class EmailAlreadyConfirmedException extends RuntimeException{
    public EmailAlreadyConfirmedException(String message) {
        super(message);
    }
    public EmailAlreadyConfirmedException(String message, Throwable cause) {
        super(message, cause);
    }
}