package com.mtogo.restaurant.domain.model;

public record MenuItem(Long itemId, String name, String description, double price) {
    public MenuItem {
        if (price <= 0)
            throw new IllegalArgumentException("Price must be positive");
    }
}
