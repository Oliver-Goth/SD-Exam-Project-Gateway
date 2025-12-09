package com.mtogo.financial_service.infrastructure.adapters.out.messaging.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.mtogo.financial_service.domain.port.out.PaymentEventPublisherPort;
import com.mtogo.financial_service.infrastructure.adapters.out.messaging.events.PaymentEvent;
import com.mtogo.financial_service.infrastructure.config.RabbitMqConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisherAdapter implements PaymentEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(PaymentEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.PAYMENT_EXCHANGE,
                RabbitMqConfig.PAYMENT_ROUTING_KEY,
                event
        );

        System.out.println("Payment event published: " + event);
    }
}
