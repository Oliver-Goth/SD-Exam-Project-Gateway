package com.mtogo.restaurant.domain.model;

public record RestaurantProfile(String name, String address, String contactInfo) {
    public RestaurantProfile {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name required");
        if (address == null || address.isBlank())
            throw new IllegalArgumentException("Address required");
        if (contactInfo == null || contactInfo.isBlank())
            throw new IllegalArgumentException("Contact required");
    }
}
