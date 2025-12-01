package com.mtogo.ordering.application.dto;

import java.math.BigDecimal;

public record OrderItemDto(Long menuItemId, String name, BigDecimal price, int quantity) {
}
