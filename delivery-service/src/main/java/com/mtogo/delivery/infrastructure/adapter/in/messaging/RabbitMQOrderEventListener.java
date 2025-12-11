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

    @RabbitListener(queues = "delivery.order.prepared")
    public void handleOrderPrepared(OrderPreparedEventDTO dto) {
        consumer.handleOrderPrepared(dto.orderId(), dto.restaurantId(), dto.customerId());
    }
}
