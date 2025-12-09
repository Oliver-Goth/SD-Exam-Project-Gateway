package com.mtogo.delivery.infrastructure.api;

import java.time.Instant;

public record DeliveryTaskResponse(Long deliveryId, Long orderId, Long restaurantId, Long customerId, Long agentId, String status, Instant pickupTime, Instant deliveryTime) {
}
