package com.mtogo.restaurant.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantTest {

    @Test
    void approveSetsStatusToApproved() {
        Restaurant restaurant = new Restaurant(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "hashedPassword",
                "12345678",
                "123 Main St");

        restaurant.approve();

        assertEquals(ApprovalStatus.APPROVED, restaurant.getApprovalStatus());
    }

    @Test
    void suspendSetsStatusToSuspended() {
        Restaurant restaurant = new Restaurant(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "hashedPassword",
                "12345678",
                "123 Main St");

        restaurant.suspend();

        assertEquals(ApprovalStatus.SUSPENDED, restaurant.getApprovalStatus());
    }

    @Test
    void newRestaurantHasPendingStatus() {
        Restaurant restaurant = new Restaurant(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "hashedPassword",
                "12345678",
                "123 Main St");

        assertEquals(ApprovalStatus.PENDING, restaurant.getApprovalStatus());
    }
}
