package com.mtogo.restaurant.application.service;

import com.mtogo.restaurant.infrastructure.adapter.out.persistence.RestaurantJpaEntity;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.RestaurantJpaRepository;
import com.mtogo.restaurant.infrastructure.security.PasswordEncoderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RestaurantService {
    private final RestaurantJpaRepository restaurantRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    public RestaurantService(RestaurantJpaRepository restaurantRepository, PasswordEncoderUtil passwordEncoderUtil) {
        this.restaurantRepository = restaurantRepository;
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

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoderUtil.matches(rawPassword, encodedPassword);
    }
}
