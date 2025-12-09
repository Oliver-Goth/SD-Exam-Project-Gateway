package com.mtogo.delivery.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeliveryTaskJpaRepository extends JpaRepository<DeliveryTaskJpaEntity, Long> {
    Optional<DeliveryTaskJpaEntity> findByDeliveryId(Long deliveryId);
    Optional<DeliveryTaskJpaEntity> findByOrderId(Long orderId);
}
