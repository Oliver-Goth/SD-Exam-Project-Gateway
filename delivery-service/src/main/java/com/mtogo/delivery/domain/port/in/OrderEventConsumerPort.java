package com.mtogo.delivery.domain.port.in;

public interface OrderEventConsumerPort {
    void handleOrderPrepared(Long orderId, Long restaurantId, Long customerId);
}
