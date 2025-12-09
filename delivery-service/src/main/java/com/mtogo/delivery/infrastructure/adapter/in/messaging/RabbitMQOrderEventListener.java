package com.mtogo.delivery.infrastructure.adapter.in.messaging;

import com.mtogo.delivery.domain.port.in.OrderEventConsumerPort;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQOrderEventListener {

    private final OrderEventConsumerPort consumer;

    public RabbitMQOrderEventListener(OrderEventConsumerPort consumer) {
        this.consumer = consumer;
    }

    @RabbitListener(queues = "delivery.order.created")
    public void handleOrderCreated(OrderCreatedEventDTO dto) {
        consumer.handleOrderCreated(dto.orderId(), dto.restaurantId(), dto.customerId());
    }
}

