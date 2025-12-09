package com.mtogo.fulfillment.domain.service;

import com.mtogo.fulfillment.domain.model.FulfillmentOrder;
import com.mtogo.fulfillment.domain.model.KitchenNote;
import com.mtogo.fulfillment.domain.model.PreparationDetails;
import com.mtogo.fulfillment.domain.port.in.CreateFulfillmentFromOrderUseCase;
import com.mtogo.fulfillment.domain.port.in.MarkFulfillmentPreparedUseCase;
import com.mtogo.fulfillment.domain.port.out.FulfillmentEventPublisherPort;
import com.mtogo.fulfillment.domain.port.out.FulfillmentRepositoryPort;

public class FulfillmentApplicationService implements CreateFulfillmentFromOrderUseCase, MarkFulfillmentPreparedUseCase {

    private final FulfillmentRepositoryPort repository;
    private final FulfillmentEventPublisherPort eventPublisher;

    public FulfillmentApplicationService(FulfillmentRepositoryPort repository,
                                         FulfillmentEventPublisherPort eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public FulfillmentOrder createFromOrder(Long orderId, Long restaurantId, PreparationDetails preparationDetails, KitchenNote kitchenNote) {
        FulfillmentOrder order = new FulfillmentOrder(orderId, restaurantId, preparationDetails, kitchenNote);
        return repository.save(order);
    }

    @Override
    public void markPrepared(Long orderId) {
        FulfillmentOrder order = repository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Fulfillment not found for order: " + orderId));
        order.markPrepared();
        repository.save(order);
        eventPublisher.publishOrderPrepared(order);
    }
}
