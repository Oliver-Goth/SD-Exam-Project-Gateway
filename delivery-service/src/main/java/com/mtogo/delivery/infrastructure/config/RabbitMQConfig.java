package com.mtogo.delivery.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String DELIVERY_EXCHANGE = "delivery.exchange";
    public static final String DELIVERY_ASSIGNED_ROUTING_KEY = "delivery.assigned";

    @Bean
    public TopicExchange deliveryExchange() {
        return new TopicExchange(DELIVERY_EXCHANGE);
    }

    @Bean
    public Queue deliveryAssignedQueue() {
        return new Queue(DELIVERY_ASSIGNED_ROUTING_KEY, true);
    }

    @Bean
    public Binding deliveryAssignedBinding(Queue deliveryAssignedQueue, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(deliveryAssignedQueue)
                .to(deliveryExchange)
                .with(DELIVERY_ASSIGNED_ROUTING_KEY);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange("order.exchange");
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue("delivery.order.created", true);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(orderExchange).with("order.created");
    }

}
