package com.mtogo.financial_service.application.services.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.PaymentStatus;

class PaymentProviderTest {

    @Test
    void processPaymentTest() {

        Payment payment = new Payment();
        payment.setAmount(100.0);

        PaymentProvider calculator = new PaymentProvider();

        calculator.processPayment(payment);

        assertNotNull(payment.getStatus());
        
        assertTrue(
            payment.getStatus() == PaymentStatus.COMPLETED ||
            payment.getStatus() == PaymentStatus.PENDING ||
            payment.getStatus() == PaymentStatus.FAILED
        );

        assertEquals("TEST", payment.getPaymentProvider());
        assertNotNull(payment.getPaymentProviderId());
        assertNotNull(payment.getUpdatedAt());
    }
}
