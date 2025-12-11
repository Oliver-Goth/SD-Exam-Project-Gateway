package com.mtogo.delivery.infrastructure.config;

import com.mtogo.delivery.application.service.AssignDeliveryService;
import com.mtogo.delivery.application.service.CreateDeliveryService;
import com.mtogo.delivery.application.service.OrderEventConsumerService;
import com.mtogo.delivery.application.service.UpdateDeliveryStatusService;
import com.mtogo.delivery.domain.port.out.DeliveryTaskRepositoryPort;
import com.mtogo.delivery.domain.port.out.DeliveryEventPublisherPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeliveryConfig {

    @Bean
    public CreateDeliveryService createDeliveryService(DeliveryTaskRepositoryPort repository) {
        return new CreateDeliveryService(repository);
    }

    @Bean
    public AssignDeliveryService assignDeliveryService(
            DeliveryTaskRepositoryPort repository,
            DeliveryEventPublisherPort eventPublisher
    ) {
        return new AssignDeliveryService(repository, eventPublisher);
    }


    @Bean
    public UpdateDeliveryStatusService updateDeliveryStatusService(
            DeliveryTaskRepositoryPort repository,
            DeliveryEventPublisherPort eventPublisher
    ) {
        return new UpdateDeliveryStatusService(repository, eventPublisher);
    }

    @Bean
    public OrderEventConsumerService orderEventConsumerService(
            CreateDeliveryService createDeliveryService
    ) {
        return new OrderEventConsumerService(createDeliveryService);
    }
}
