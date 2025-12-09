package com.mtogo.financial_service.application.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    
    public PaymentNotFoundException(Long id) {
        super("Payment with ID: " + id + " was not found.");
    }

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
