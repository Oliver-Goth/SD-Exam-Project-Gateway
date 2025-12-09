package com.mtogo.delivery.domain.port.in;

public interface OrderEventConsumerPort {
    void handleOrderCreated(Long orderId, Long restaurantId, Long customerId);
}
