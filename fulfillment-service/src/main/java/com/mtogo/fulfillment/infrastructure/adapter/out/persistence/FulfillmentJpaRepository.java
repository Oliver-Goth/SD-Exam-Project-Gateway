package com.mtogo.fulfillment.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FulfillmentJpaRepository extends JpaRepository<FulfillmentJpaEntity, Long> {
    Optional<FulfillmentJpaEntity> findByOrderId(Long orderId);
}
