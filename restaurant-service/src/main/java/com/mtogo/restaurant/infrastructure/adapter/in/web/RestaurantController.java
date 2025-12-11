package com.mtogo.restaurant.infrastructure.adapter.in.web;

import com.mtogo.restaurant.application.dto.LoginRequest;
import com.mtogo.restaurant.application.dto.LoginResponse;
import com.mtogo.restaurant.application.service.RestaurantService;
import com.mtogo.restaurant.application.service.RestaurantService.MenuItemRequest;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.MenuJpaEntity;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.MenuItemJpaEntity;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.RestaurantJpaEntity;
import com.mtogo.restaurant.infrastructure.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/{restaurantId}/menu")
    @Operation(summary = "Create menu with items for a restaurant")
    public ResponseEntity<MenuResponse> createMenu(@PathVariable Long restaurantId,
                                                   @RequestBody MenuCreateRequest request,
                                                   Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        Long authRestaurantId = (Long) authentication.getPrincipal();
        if (!restaurantId.equals(authRestaurantId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot create menu for another restaurant");
        }

        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu items are required");
        }

        MenuJpaEntity menu = restaurantService.createMenu(
                restaurantId,
                request.items().stream()
                        .map(i -> new MenuItemRequest(i.name(), i.description(), i.price()))
                        .toList()
        );

        MenuResponse response = MenuResponse.fromEntity(menu);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List restaurants")
    public ResponseEntity<List<RestaurantResponse>> listRestaurants() {
        List<RestaurantResponse> restaurants = restaurantService.findAll().stream()
                .map(RestaurantResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{restaurantId}")
    @Operation(summary = "Get restaurant by id")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable Long restaurantId) {
        RestaurantJpaEntity restaurant = restaurantService.findById(restaurantId);
        return ResponseEntity.ok(RestaurantResponse.fromEntity(restaurant));
    }
}

record RestaurantRequest(String name, String ownerName, String email, String password, String phone, String address) {
}

record RestaurantResponse(Long id, String name, String ownerName, String email, String phone, String address, String approvalStatus) {
    static RestaurantResponse fromEntity(RestaurantJpaEntity e) {
        return new RestaurantResponse(
                e.getId(),
                e.getName(),
                e.getOwnerName(),
                e.getEmail(),
                e.getPhone(),
                e.getAddress(),
                e.getApprovalStatus()
        );
    }
}

record MenuCreateRequest(List<MenuItemDto> items) { }

record MenuItemDto(String name, String description, double price) { }

record MenuResponse(Long id, String status, List<MenuItemResponse> items) {
    static MenuResponse fromEntity(MenuJpaEntity entity) {
        return new MenuResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getItems().stream()
                        .map(MenuItemResponse::fromEntity)
                        .toList()
        );
    }
}

record MenuItemResponse(Long id, String name, String description, double price) {
    static MenuItemResponse fromEntity(MenuItemJpaEntity entity) {
        return new MenuItemResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice()
        );
    }
}
