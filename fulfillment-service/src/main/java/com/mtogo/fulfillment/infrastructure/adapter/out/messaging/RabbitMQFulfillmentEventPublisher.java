package com.mtogo.fulfillment.infrastructure.adapter.out.messaging;

import com.mtogo.fulfillment.domain.model.FulfillmentOrder;
import com.mtogo.fulfillment.domain.port.out.FulfillmentEventPublisherPort;
import com.mtogo.fulfillment.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQFulfillmentEventPublisher implements FulfillmentEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQFulfillmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishOrderPrepared(FulfillmentOrder order) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_PREPARED_ROUTING_KEY,
                new OrderPreparedMessage(order.getOrderId(), order.getRestaurantId()));
    }

    public record OrderPreparedMessage(Long orderId, Long restaurantId) {}
}
