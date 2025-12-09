package com.mtogo.financial_service.infrastructure.adapters.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mtogo.financial_service.domain.model.Commission;
import com.mtogo.financial_service.domain.port.out.CommissionRepositoryPort;

@Component
public class CommissionRepositoryAdapter implements CommissionRepositoryPort {

    private final CommissionJpaRepository repository;

    public CommissionRepositoryAdapter(CommissionJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Commission> findByPaymentId(Long paymentId) {
        return repository.findByPaymentId(paymentId);
    }
}
