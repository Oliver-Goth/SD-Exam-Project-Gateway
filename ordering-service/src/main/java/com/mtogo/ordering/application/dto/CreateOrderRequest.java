package com.mtogo.ordering.application.dto;

import java.util.List;

public record CreateOrderRequest(
        Long customerId,
        Long restaurantId,
        List<CreateOrderItemRequest> items) {

    public record CreateOrderItemRequest(
            Long menuItemId,
            String name,
            String price,
            int quantity
    ) {
    }
}
