package com.mtogo.financial_service.infrastructure.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mtogo.financial_service.domain.model.Payment;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    List<Payment> findAllByOrderId(Long orderId);

    // List<Payment> findAllByRestaurantId(Long restaurantId);
    
}

