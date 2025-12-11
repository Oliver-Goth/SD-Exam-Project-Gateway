package com.mtogo.ordering.infrastructure.adapter.out.messaging;

import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import com.mtogo.ordering.domain.port.out.OrderEventPublisherPort;
import com.mtogo.ordering.infrastructure.config.RabbitMQConfig;
import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQOrderEventPublisher implements OrderEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQOrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishOrderConfirmed(Order order) {
        Map<String, Object> payload = Map.of(
                "orderId", order.getId(),
                "customerId", order.getCustomerId(),
                "restaurantId", order.getRestaurantId(),
                "total", order.getTotal().toPlainString(),
                "items", order.getItems().stream().map(this::toItemPayload).toList()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_CONFIRMED_ROUTING_KEY,
                payload
        );
    }

    private Map<String, Object> toItemPayload(OrderItem i) {
        return Map.of(
                "menuItemId", i.getMenuItemId(),
                "name", i.getName(),
                "price", i.getPrice().toPlainString(),
                "quantity", i.getQuantity()
        );
    }
}
