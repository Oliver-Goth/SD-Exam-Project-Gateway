package com.mtogo.delivery.domain.port.in;

import com.mtogo.delivery.domain.model.DeliveryTask;

public interface CreateDeliveryUseCase {
    DeliveryTask createForOrder(Long orderId, Long restaurantId, Long customerId);
}
