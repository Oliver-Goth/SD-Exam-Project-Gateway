package com.mtogo.customer.application.exception;

public class InvalidVerificationTokenException extends RuntimeException {

    public InvalidVerificationTokenException() {
        super("Verification token is not valid");
    }

    public InvalidVerificationTokenException(String message) {
        super(message);
    }
}
