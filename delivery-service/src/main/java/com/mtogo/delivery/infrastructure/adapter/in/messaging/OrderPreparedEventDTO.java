package com.mtogo.delivery.infrastructure.adapter.in.messaging;

public record OrderPreparedEventDTO(Long orderId, Long restaurantId, Long customerId) { }
