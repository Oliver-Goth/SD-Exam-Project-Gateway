package com.mtogo.delivery.domain.port.out;

public interface DeliveryEventPublisherPort {
    void publishDeliveryAssigned(Long deliveryId, Long orderId, Long agentId);
    void publishDeliveryDelivered(Long deliveryId, Long orderId);
}
