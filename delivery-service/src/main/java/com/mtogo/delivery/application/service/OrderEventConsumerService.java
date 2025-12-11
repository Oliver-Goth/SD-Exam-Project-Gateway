package com.mtogo.delivery.application.service;

import com.mtogo.delivery.domain.port.in.OrderEventConsumerPort;
import com.mtogo.delivery.domain.port.in.CreateDeliveryUseCase;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumerService implements OrderEventConsumerPort {

    private final CreateDeliveryUseCase createDeliveryUseCase;

    public OrderEventConsumerService(CreateDeliveryUseCase createDeliveryUseCase) {
        this.createDeliveryUseCase = createDeliveryUseCase;
    }

    @Override
    public void handleOrderPrepared(Long orderId, Long restaurantId, Long customerId) {
        createDeliveryUseCase.createForOrder(orderId, restaurantId, customerId);
    }
}
