package com.mtogo.delivery.application.service;

import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.port.out.DeliveryTaskRepositoryPort;
import com.mtogo.delivery.domain.port.out.DeliveryEventPublisherPort;
import org.springframework.stereotype.Service;

@Service
public class AssignDeliveryService {

    private final DeliveryTaskRepositoryPort deliveryTaskRepository;
    private final DeliveryEventPublisherPort eventPublisher;

    public AssignDeliveryService(DeliveryTaskRepositoryPort deliveryTaskRepository, DeliveryEventPublisherPort eventPublisher) {
        this.deliveryTaskRepository = deliveryTaskRepository;
        this.eventPublisher = eventPublisher;
    }

    public DeliveryTask assignAgent(Long deliveryTaskId, Long agentId) {
        DeliveryTask task = deliveryTaskRepository.findById(deliveryTaskId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "DeliveryTask not found: " + deliveryTaskId
                ));

        if (agentId == null){
            return task;
        }

        task.assignAgent(agentId);

        DeliveryTask updatedTask = deliveryTaskRepository.save(task);

        eventPublisher.publishDeliveryAssigned(
                updatedTask.getDeliveryId(),
                updatedTask.getOrderId(),
                agentId
        );

        return updatedTask;
    }
}
