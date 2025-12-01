package com.mtogo.ordering.domain.port.in;

import java.util.List;

public record CreateOrderCommand(
        Long customerId,
        Long restaurantId,
        List<CreateOrderItem> items) {

    public CreateOrderCommand {
        items = items == null ? List.of() : List.copyOf(items);
    }

    public record CreateOrderItem(Long menuItemId, String name, String price, int quantity) {
    }
}
