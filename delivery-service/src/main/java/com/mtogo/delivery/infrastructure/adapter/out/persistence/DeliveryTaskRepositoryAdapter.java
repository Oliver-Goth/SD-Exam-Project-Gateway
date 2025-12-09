package com.mtogo.delivery.infrastructure.adapter.out.persistence;

import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.port.out.DeliveryTaskRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DeliveryTaskRepositoryAdapter implements DeliveryTaskRepositoryPort {

    private final DeliveryTaskJpaRepository jpa;
    private final DeliveryTaskMapper mapper = new DeliveryTaskMapper();

    public DeliveryTaskRepositoryAdapter(DeliveryTaskJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public DeliveryTask save(DeliveryTask task) {
        DeliveryTaskJpaEntity e = mapper.toEntity(task);
        DeliveryTaskJpaEntity saved = jpa.save(e);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<DeliveryTask> findById(Long deliveryId) {
        return jpa.findByDeliveryId(deliveryId).map(mapper::toDomain);
    }

    @Override
    public Optional<DeliveryTask> findByOrderId(Long orderId) {
        return jpa.findByOrderId(orderId).map(mapper::toDomain);
    }
}
