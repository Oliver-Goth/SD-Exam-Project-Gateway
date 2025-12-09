package com.mtogo.financial_service.application.exceptions;

public class RestaurantNotFoundException extends RuntimeException {
    
    public RestaurantNotFoundException(Long id) {
        super("Restaurant with ID " + id + " was either not found or does not have any payments.");
    }

    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
