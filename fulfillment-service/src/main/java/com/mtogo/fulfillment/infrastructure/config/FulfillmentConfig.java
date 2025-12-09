package com.mtogo.fulfillment.infrastructure.config;

import com.mtogo.fulfillment.domain.port.out.FulfillmentEventPublisherPort;
import com.mtogo.fulfillment.domain.port.out.FulfillmentRepositoryPort;
import com.mtogo.fulfillment.domain.service.FulfillmentApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FulfillmentConfig {

    @Bean
    public FulfillmentApplicationService fulfillmentApplicationService(FulfillmentRepositoryPort repository,
                                                                       FulfillmentEventPublisherPort eventPublisher) {
        return new FulfillmentApplicationService(repository, eventPublisher);
    }
}
