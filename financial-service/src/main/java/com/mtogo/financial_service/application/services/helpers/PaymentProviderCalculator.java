package com.mtogo.financial_service.application.services.helpers;

import java.util.Random;

import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.PaymentStatus;
import com.mtogo.financial_service.domain.port.in.ConfirmPaymentProvider;

public class PaymentProviderCalculator implements ConfirmPaymentProvider {

    private final Random random = new Random();

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
        payment.setPaymentProvider("PayPal");
        payment.setPaymentProviderId("paypal_txn_" + System.currentTimeMillis());
        payment.setUpdatedAt(java.time.LocalDateTime.now());
    }
}
