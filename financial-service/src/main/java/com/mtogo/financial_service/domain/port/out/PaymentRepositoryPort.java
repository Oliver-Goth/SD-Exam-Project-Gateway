package com.mtogo.financial_service.domain.port.out;    

import java.util.Optional;

import com.mtogo.financial_service.domain.model.Payment;

public interface PaymentRepositoryPort {

    Payment save(Payment payment);

    Optional<Payment> findByPaymentId(Long id);

    Optional<Payment> getStatus (Long id);

}
