package com.mtogo.restaurant.infrastructure.adapter.in.web;

import com.example.AuditLogger;
import com.example.RequestLogger;
import com.example.TimeIt;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService restaurantService;
    private final JwtTokenProvider jwtTokenProvider;

    public RestaurantController(RestaurantService restaurantService, JwtTokenProvider jwtTokenProvider) {
        this.restaurantService = restaurantService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/onboard")
    @Operation(summary = "Onboard new restaurant")
    public ResponseEntity<Long> onboardRestaurant(@RequestBody RestaurantRequest request) {
        RestaurantJpaEntity restaurant = RequestLogger.log(
                request.email(),
                "POST",
                "/api/restaurants/onboard",
                201,
                () -> {
                    RestaurantJpaEntity created = TimeIt.info(log, "Create restaurant",
                            () -> restaurantService.createRestaurant(
                                    request.name(),
                                    request.ownerName(),
                                    request.email(),
                                    request.password(),
                                    request.phone(),
                                    request.address()));

                    AuditLogger.log(
                            "RESTAURANT_ONBOARD",
                            request.email(),
                            "restaurant:" + created.getId(),
                            "203.0.113.7");

                    return created;
                });

        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant.getId());
    }

    @PostMapping("/login")
    @Operation(summary = "Login restaurant owner")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = RequestLogger.log(
                request.email(),
                "POST",
                "/api/restaurants/login",
                200,
                () -> {
                    if (request.email() == null || request.password() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
                    }

                    RestaurantJpaEntity restaurant = TimeIt.info(log, "Authenticate restaurant",
                            () -> restaurantService.findByEmail(request.email()));

                    if (!restaurantService.validatePassword(request.password(), restaurant.getPassword())) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
                    }

                    String token = jwtTokenProvider.generateToken(restaurant.getId(), restaurant.getEmail());

                    AuditLogger.log(
                            "RESTAURANT_LOGIN",
                            request.email(),
                            "restaurant:" + restaurant.getId(),
                            "203.0.113.7");

                    return new LoginResponse(restaurant.getId(), token);
                });

        return ResponseEntity.ok(response);
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
        MenuResponse response = RequestLogger.log(
                String.valueOf(restaurantId),
                "POST",
                "/api/restaurants/{restaurantId}/menu",
                201,
                () -> {
                    if (authentication == null || authentication.getPrincipal() == null) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
                    }

                    Long authRestaurantId = (Long) authentication.getPrincipal();
                    if (!restaurantId.equals(authRestaurantId)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                "Cannot create menu for another restaurant");
                    }

                    if (request.items() == null || request.items().isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu items are required");
                    }

                    MenuJpaEntity menu = TimeIt.info(log, "Create menu", () -> restaurantService.createMenu(
                            restaurantId,
                            request.items().stream()
                                    .map(i -> new MenuItemRequest(i.name(), i.description(), i.price()))
                                    .toList()));

                    AuditLogger.log(
                            "MENU_CREATE",
                            String.valueOf(restaurantId),
                            "menu:" + menu.getId(),
                            "203.0.113.7");

                    return MenuResponse.fromEntity(menu);
                });

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List restaurants")
    public ResponseEntity<List<RestaurantResponse>> listRestaurants() {
        List<RestaurantResponse> restaurants = RequestLogger.log(
                "anonymous",
                "GET",
                "/api/restaurants",
                200,
                () -> TimeIt.info(log, "List restaurants", () -> restaurantService.findAll().stream()
                        .map(RestaurantResponse::fromEntity)
                        .collect(Collectors.toList())));

        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{restaurantId}")
    @Operation(summary = "Get restaurant by id")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable Long restaurantId) {
        RestaurantResponse response = RequestLogger.log(
                String.valueOf(restaurantId),
                "GET",
                "/api/restaurants/{restaurantId}",
                200,
                () -> TimeIt.info(log, "Get restaurant by id", () -> {
                    RestaurantJpaEntity restaurant = restaurantService.findById(restaurantId);
                    return RestaurantResponse.fromEntity(restaurant);
                }));

        return ResponseEntity.ok(response);
    }
}

record RestaurantRequest(String name, String ownerName, String email, String password, String phone, String address) {
}

record RestaurantResponse(Long id, String name, String ownerName, String email, String phone, String address,
        String approvalStatus) {
    static RestaurantResponse fromEntity(RestaurantJpaEntity e) {
        return new RestaurantResponse(
                e.getId(),
                e.getName(),
                e.getOwnerName(),
                e.getEmail(),
                e.getPhone(),
                e.getAddress(),
                e.getApprovalStatus());
    }
}

record MenuCreateRequest(List<MenuItemDto> items) {
}

record MenuItemDto(String name, String description, double price) {
}

record MenuResponse(Long id, String status, List<MenuItemResponse> items) {
    static MenuResponse fromEntity(MenuJpaEntity entity) {
        return new MenuResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getItems().stream()
                        .map(MenuItemResponse::fromEntity)
                        .toList());
    }
}

record MenuItemResponse(Long id, String name, String description, double price) {
    static MenuItemResponse fromEntity(MenuItemJpaEntity entity) {
        return new MenuItemResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice());
    }
}
