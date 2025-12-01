package com.mtogo.customer.application.exception;

public class CustomerNotVerifiedException extends RuntimeException {

    public CustomerNotVerifiedException(String message) {
        super(message);
    }
}
