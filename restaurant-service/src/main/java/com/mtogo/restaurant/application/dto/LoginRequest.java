package com.mtogo.restaurant.application.dto;

public record LoginRequest(
        String email,
        String password) {
}
