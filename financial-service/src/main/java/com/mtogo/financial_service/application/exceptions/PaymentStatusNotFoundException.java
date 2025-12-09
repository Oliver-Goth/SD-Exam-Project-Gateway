package com.mtogo.financial_service.application.exceptions;

public class PaymentStatusNotFoundException extends RuntimeException {
    
    public PaymentStatusNotFoundException(Long id) {
        super("Status for payment with ID: " + id + " was not found.");
    }

    public PaymentStatusNotFoundException(String message) {
        super(message);
    }
}
