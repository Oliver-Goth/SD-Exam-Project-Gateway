package com.mtogo.financial_service.application.exceptions;

public class PaymentNotCreatedException extends RuntimeException {

    public PaymentNotCreatedException(Long orderId) {
        super("Payment could not be created for orderId: " + orderId);
    }

    public PaymentNotCreatedException(String message) {
        super(message);
    }
}
