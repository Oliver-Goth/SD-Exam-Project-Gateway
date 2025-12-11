package com.mtogo.financial_service.infrastructure.adapters.out.persistence;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mtogo.financial_service.domain.model.Commission;

public interface CommissionJpaRepository extends JpaRepository<Commission, Long> {

    List<Commission> findByPaymentId(Long paymentId);
    
}
