package com.mtogo.delivery.infrastructure.adapter.out.messaging;

import com.mtogo.delivery.domain.port.out.DeliveryEventPublisherPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQDeliveryEventPublisherAdapter implements DeliveryEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQDeliveryEventPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishDeliveryAssigned(Long deliveryId, Long orderId, Long agentId) {
        var payload = new DeliveryAssignedEvent(deliveryId, orderId, agentId);
        rabbitTemplate.convertAndSend("delivery.exchange", "delivery.assigned", payload);
    }

    @Override
    public void publishDeliveryDelivered(Long deliveryId, Long orderId) {
        var payload = new DeliveryDeliveredEvent(deliveryId, orderId);
        rabbitTemplate.convertAndSend("delivery.exchange", "delivery.delivered", payload);
    }

    public record DeliveryAssignedEvent(Long deliveryId, Long orderId, Long agentId) {}
    public record DeliveryDeliveredEvent(Long deliveryId, Long orderId) {}
}
