package com.mtogo.restaurant.domain.model;

public class Menu {
    private Long menuId;
    private final Long restaurantId;
    private int version = 1;
    private MenuStatus status = MenuStatus.DRAFT;

    public Menu(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void publish() {
        this.status = MenuStatus.PUBLISHED;
    }

    public void archive() {
        this.status = MenuStatus.ARCHIVED;
    }

    public MenuStatus getStatus() {
        return status;
    }
}
