package com.mtogo.financial_service.domain.port.in;

import com.mtogo.financial_service.domain.model.Payment;

public interface ConfirmPaymentProvider {

    void processPayment(Payment payment);
    
}