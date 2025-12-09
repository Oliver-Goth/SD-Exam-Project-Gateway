package com.mtogo.delivery.infrastructure.api;

public record DeliveryTaskRequest(Long orderId, Long restaurantId, Long customerId) { }
