package com.mtogo.customer.infrastructure.adapter.out.messaging;

import com.mtogo.customer.domain.port.out.CustomerEventPublisherPort;
import com.mtogo.customer.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomerEventPublisherAdapter implements CustomerEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishCustomerRegistered(Long id, String email) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", id);
        payload.put("email", email);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUSTOMER_EXCHANGE,
                RabbitMQConfig.CUSTOMER_REGISTERED,
                payload
        );

        System.out.println("Published event: customer.registered");
    }

    @Override
    public void publishCustomerVerified(Long id, String email) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", id);
        payload.put("email", email);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.CUSTOMER_EXCHANGE,
                RabbitMQConfig.CUSTOMER_VERIFIED,
                payload
        );

        System.out.println("Published event: customer.verified");
    }
}
