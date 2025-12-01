package com.mtogo.ordering.infrastructure.config;

import com.mtogo.ordering.domain.port.in.ConfirmOrderUseCase;
import com.mtogo.ordering.domain.port.in.CreateOrderUseCase;
import com.mtogo.ordering.domain.port.in.GetOrderUseCase;
import com.mtogo.ordering.domain.port.out.OrderEventPublisherPort;
import com.mtogo.ordering.domain.port.out.OrderRepositoryPort;
import com.mtogo.ordering.domain.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderingConfig {

    @Bean
    public OrderService orderService(OrderRepositoryPort repository,
                                     OrderEventPublisherPort eventPublisher) {
        return new OrderService(repository, eventPublisher);
    }
}
