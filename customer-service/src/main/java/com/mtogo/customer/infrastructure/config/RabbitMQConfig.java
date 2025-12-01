package com.mtogo.customer.infrastructure.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CUSTOMER_EXCHANGE = "customer.exchange";
    public static final String CUSTOMER_REGISTERED = "customer.registered";
    public static final String CUSTOMER_VERIFIED = "customer.verified";

    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(CUSTOMER_EXCHANGE);
    }

    @Bean
    public Queue customerRegisteredQueue() {
        return new Queue(CUSTOMER_REGISTERED, true);
    }

    @Bean
    public Queue customerVerifiedQueue() {
        return new Queue(CUSTOMER_VERIFIED, true);
    }
}
