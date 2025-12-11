package com.mtogo.delivery.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
        // Shared order exchange from Ordering/Fulfillment contexts
        return new TopicExchange("order.events");
    }

    @Bean
    public Queue orderPreparedQueue() {
        return new Queue("delivery.order.prepared", true);
    }

    @Bean
    public Binding orderPreparedBinding(Queue orderPreparedQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderPreparedQueue).to(orderExchange).with("order.prepared");
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
