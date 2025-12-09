package com.mtogo.delivery.application.service;

import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.model.Location;
import com.mtogo.delivery.domain.model.TrackingInfo;
import com.mtogo.delivery.domain.port.in.UpdateDeliveryStatusUseCase;
import com.mtogo.delivery.domain.port.out.DeliveryEventPublisherPort;
import com.mtogo.delivery.domain.port.out.DeliveryTaskRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateDeliveryStatusService implements UpdateDeliveryStatusUseCase {

    private final DeliveryTaskRepositoryPort repository;
    private final DeliveryEventPublisherPort eventPublisher;

    public UpdateDeliveryStatusService(DeliveryTaskRepositoryPort repository, DeliveryEventPublisherPort eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public DeliveryTask assignAgent(Long deliveryId, Long agentId) {
        DeliveryTask task = repository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery not found"));
        task.assignAgent(agentId);
        DeliveryTask saved = repository.save(task);
        eventPublisher.publishDeliveryAssigned(saved.getDeliveryId(), saved.getOrderId(), saved.getAgentId());
        return saved;
    }

    @Override
    public DeliveryTask markPickedUp(Long deliveryId, Instant pickupTime, Location pickupLocation) {
        DeliveryTask task = repository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery not found"));
        task.markPickedUp(pickupTime, pickupLocation);
        return repository.save(task);
    }

    @Override
    public DeliveryTask markDelivered(Long deliveryId, Instant deliveryTime, Location deliveryLocation) {
        DeliveryTask task = repository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery not found"));
        task.markDelivered(deliveryTime, deliveryLocation);
        DeliveryTask saved = repository.save(task);
        eventPublisher.publishDeliveryDelivered(saved.getDeliveryId(), saved.getOrderId());
        return saved;
    }

    @Override
    public DeliveryTask setTrackingInfo(Long deliveryId, TrackingInfo trackingInfo) {
        DeliveryTask task = repository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery not found"));
        task.setTrackingInfo(trackingInfo);
        return repository.save(task);
    }
}
