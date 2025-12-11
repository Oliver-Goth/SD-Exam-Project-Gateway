package com.mtogo.restaurant.application.service;

import com.mtogo.restaurant.infrastructure.adapter.out.persistence.MenuJpaEntity;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.MenuJpaRepository;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.RestaurantJpaEntity;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.RestaurantJpaRepository;
import com.mtogo.restaurant.infrastructure.security.PasswordEncoderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class RestaurantService {
    private final RestaurantJpaRepository restaurantRepository;
    private final MenuJpaRepository menuRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    public RestaurantService(RestaurantJpaRepository restaurantRepository,
                             MenuJpaRepository menuRepository,
                             PasswordEncoderUtil passwordEncoderUtil) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.passwordEncoderUtil = passwordEncoderUtil;
    }

    public RestaurantJpaEntity createRestaurant(String name, String ownerName, String email, String password,
            String phone, String address) {
        String hashedPassword = passwordEncoderUtil.encode(password);
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity(name, ownerName, email, hashedPassword, phone,
                address);
        return restaurantRepository.save(restaurant);
    }

    public RestaurantJpaEntity findByEmail(String email) {
        return restaurantRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    }

    public RestaurantJpaEntity findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    }

    public List<RestaurantJpaEntity> findAll() {
        return restaurantRepository.findAll();
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoderUtil.matches(rawPassword, encodedPassword);
    }

    public MenuJpaEntity createMenu(Long restaurantId, List<MenuItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu items are required");
        }

        RestaurantJpaEntity restaurant = findById(restaurantId);

        MenuJpaEntity menu = new MenuJpaEntity(restaurant);
        items.stream()
                .filter(Objects::nonNull)
                .forEach(item -> {
                    if (item.price() <= 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must be positive");
                    }
                    menu.addItem(item.name(), item.description(), item.price());
                });

        return menuRepository.save(menu);
    }

    public record MenuItemRequest(String name, String description, double price) { }
}
