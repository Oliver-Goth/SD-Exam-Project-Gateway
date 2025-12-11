package com.mtogo.ordering.application.dto;

import com.mtogo.ordering.domain.model.OrderStatus;
import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        Long restaurantId,
        List<OrderItemDto> items,
        BigDecimal total,
        OrderStatus status) {
}
