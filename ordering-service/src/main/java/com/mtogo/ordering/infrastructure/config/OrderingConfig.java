package com.mtogo.ordering.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mtogo.ordering.domain.port.out.OrderEventPublisherPort;
import com.mtogo.ordering.domain.port.out.OrderRepositoryPort;
import com.mtogo.ordering.domain.service.OrderService;

@Configuration
public class OrderingConfig {

    @Bean
    public OrderService orderService(OrderRepositoryPort repository,
                                     OrderEventPublisherPort eventPublisher) {
        return new OrderService(repository, eventPublisher);
    }
}