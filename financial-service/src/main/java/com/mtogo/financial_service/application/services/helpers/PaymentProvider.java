package com.mtogo.financial_service.application.services.helpers;

import java.util.Random;

import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.PaymentStatus;
import com.mtogo.financial_service.domain.port.in.ConfirmPaymentProvider;

public class PaymentProvider implements ConfirmPaymentProvider {

    private final Random random = new Random();

    // This is a mockish implementation simulating interaction with a payment provider API (The idea in real life would be to use PayPal or such)
    // Also simulates different outcomes based on random chance (if payments completes, fails or is pending)
    @Override
    public void processPayment(Payment payment) {
        try {
            Thread.sleep(500 + random.nextInt(1500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int chance = random.nextInt(100);
        PaymentStatus status;
        if (chance < 80) {
            status = PaymentStatus.COMPLETED;
        } else if (chance < 90) {
            status = PaymentStatus.PENDING; 
        } else {
            status = PaymentStatus.FAILED;
        }

        payment.setStatus(status);
        payment.setPaymentProvider("TEST");
        payment.setPaymentProviderId("TEST" + System.currentTimeMillis());
        payment.setUpdatedAt(java.time.LocalDateTime.now());
    }
}
