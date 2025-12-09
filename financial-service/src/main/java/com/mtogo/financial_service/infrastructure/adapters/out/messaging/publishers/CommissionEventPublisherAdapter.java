
package com.mtogo.financial_service.infrastructure.adapters.out.messaging.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.mtogo.financial_service.domain.port.out.CommissionEventPublisherPort;
import com.mtogo.financial_service.infrastructure.adapters.out.messaging.events.CommissionEvent;
import com.mtogo.financial_service.infrastructure.config.RabbitMqConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommissionEventPublisherAdapter implements CommissionEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(CommissionEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.ORDER_EXCHANGE, 
                RabbitMqConfig.ORDER_ROUTING_KEY, 
                event
        );

        System.out.println("Commission event published: " + event);
    }
} 
