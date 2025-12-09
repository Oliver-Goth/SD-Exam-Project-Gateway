package com.mtogo.delivery.infrastructure.adapter.in.messaging;

public record OrderCreatedEventDTO(Long orderId, Long restaurantId, Long customerId) {
}
