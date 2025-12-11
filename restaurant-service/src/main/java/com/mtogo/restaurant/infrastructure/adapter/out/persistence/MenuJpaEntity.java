package com.mtogo.restaurant.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
public class MenuJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    private RestaurantJpaEntity restaurant;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItemJpaEntity> items = new ArrayList<>();

    @Column(nullable = false)
    private String status = "DRAFT";

    public MenuJpaEntity() {
    }

    public MenuJpaEntity(RestaurantJpaEntity restaurant) {
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public RestaurantJpaEntity getRestaurant() {
        return restaurant;
    }

    public List<MenuItemJpaEntity> getItems() {
        return items;
    }

    public String getStatus() {
        return status;
    }

    public void publish() {
        this.status = "PUBLISHED";
    }

    public void addItem(String name, String description, double price) {
        MenuItemJpaEntity item = new MenuItemJpaEntity(this, name, description, price);
        this.items.add(item);
    }
}
