package com.mtogo.financial_service.infrastructure.adapters.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.port.out.PaymentRepositoryPort;

@Component
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository repository;

    public PaymentRepositoryAdapter(PaymentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment save(Payment payment) {
        return repository.save(payment); 
    }

    @Override
    public Optional<Payment> findByPaymentId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Payment> getStatus(Long paymentId) {
        return repository.findById(paymentId);
    }

    /* 
    @Override
    public List<Payment> findAllByRestaurantId(Long restaurantId) {
        return repository.findAllByRestaurantId(restaurantId);
    } */
}
