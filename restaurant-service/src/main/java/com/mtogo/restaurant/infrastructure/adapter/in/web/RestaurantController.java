package com.mtogo.restaurant.infrastructure.adapter.in.web;

import com.mtogo.restaurant.application.dto.LoginRequest;
import com.mtogo.restaurant.application.dto.LoginResponse;
import com.mtogo.restaurant.application.service.RestaurantService;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.RestaurantJpaEntity;
import com.mtogo.restaurant.infrastructure.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurants", description = "Restaurant Management APIs")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final JwtTokenProvider jwtTokenProvider;

    public RestaurantController(RestaurantService restaurantService, JwtTokenProvider jwtTokenProvider) {
        this.restaurantService = restaurantService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/onboard")
    @Operation(summary = "Onboard new restaurant")
    public ResponseEntity<Long> onboardRestaurant(@RequestBody RestaurantRequest request) {
        RestaurantJpaEntity restaurant = restaurantService.createRestaurant(
                request.name(),
                request.ownerName(),
                request.email(),
                request.password(),
                request.phone(),
                request.address());
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant.getId());
    }

    @PostMapping("/login")
    @Operation(summary = "Login restaurant owner")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request.email() == null || request.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        RestaurantJpaEntity restaurant = restaurantService.findByEmail(request.email());

        if (!restaurantService.validatePassword(request.password(), restaurant.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        String token = jwtTokenProvider.generateToken(restaurant.getId(), restaurant.getEmail());
        return ResponseEntity.ok(new LoginResponse(restaurant.getId(), token));
    }

    @PostMapping("/{restaurantId}/approve")
    @Operation(summary = "Approve restaurant")
    public ResponseEntity<Void> approveRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{restaurantId}/menu/publish")
    @Operation(summary = "Publish menu")
    public ResponseEntity<Void> publishMenu(@PathVariable Long restaurantId) {
        return ResponseEntity.ok().build();
    }
}

record RestaurantRequest(String name, String ownerName, String email, String password, String phone, String address) {
}
