package com.mtogo.restaurant.application.dto;

public record LoginResponse(
        Long restaurantId,
        String token) {
}
