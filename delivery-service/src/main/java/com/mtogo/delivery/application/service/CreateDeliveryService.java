package com.mtogo.delivery.application.service;

import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.port.in.CreateDeliveryUseCase;
import com.mtogo.delivery.domain.port.out.DeliveryTaskRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class CreateDeliveryService implements CreateDeliveryUseCase {

    private final DeliveryTaskRepositoryPort repository;

    public CreateDeliveryService(DeliveryTaskRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public DeliveryTask createForOrder(Long orderId, Long restaurantId, Long customerId) {
        DeliveryTask task = DeliveryTask.createForOrder(null, orderId, restaurantId, customerId);
        DeliveryTask saved = repository.save(task);

        return saved;

    }
}
