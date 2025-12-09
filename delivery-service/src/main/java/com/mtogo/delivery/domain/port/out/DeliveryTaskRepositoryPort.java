package com.mtogo.delivery.domain.port.out;

import com.mtogo.delivery.domain.model.DeliveryTask;

import java.util.Optional;

public interface DeliveryTaskRepositoryPort {
    DeliveryTask save(DeliveryTask task);
    Optional<DeliveryTask> findById(Long deliveryId);
    Optional<DeliveryTask> findByOrderId(Long orderId);
}
