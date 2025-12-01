package com.mtogo.customer.application.exception;

public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException(String email) {
        super("Email is already used: " + email);
    }
}
