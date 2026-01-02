package com.mtogo.restaurant.infrastructure.adapter.in.web;

import com.mtogo.restaurant.application.service.RestaurantService;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.MenuJpaEntity;
import com.mtogo.restaurant.infrastructure.adapter.out.persistence.RestaurantJpaEntity;
import com.mtogo.restaurant.infrastructure.security.JwtAuthenticationFilter;
import com.mtogo.restaurant.infrastructure.security.JwtTokenProvider;
import com.mtogo.restaurant.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RestaurantController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({ SecurityConfig.class, JwtAuthenticationFilter.class })
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void onboardRestaurantReturnsCreatedRestaurantId() throws Exception {
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "password",
                "12345678",
                "123 Main St");
        // Set ID via reflection or create a constructor that accepts it
        when(restaurantService.createRestaurant(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(restaurant);

        String payload = """
                {
                  "name": "Test Restaurant",
                  "ownerName": "John Doe",
                  "email": "test@restaurant.com",
                  "password": "password123",
                  "phone": "12345678",
                  "address": "123 Main St"
                }
                """;

        mockMvc.perform(post("/api/restaurants/onboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    void loginReturnsTokenWhenCredentialsValid() throws Exception {
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "encodedPassword",
                "12345678",
                "123 Main St");

        when(restaurantService.findByEmail("test@restaurant.com")).thenReturn(restaurant);
        when(restaurantService.validatePassword("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(), anyString())).thenReturn("jwt-token-123");

        String payload = """
                {
                  "email": "test@restaurant.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/restaurants/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"));
    }

    @Test
    void loginReturnsUnauthorizedWhenPasswordInvalid() throws Exception {
        RestaurantJpaEntity restaurant = new RestaurantJpaEntity(
                "Test Restaurant",
                "John Doe",
                "test@restaurant.com",
                "encodedPassword",
                "12345678",
                "123 Main St");

        when(restaurantService.findByEmail("test@restaurant.com")).thenReturn(restaurant);
        when(restaurantService.validatePassword("wrongPassword", "encodedPassword")).thenReturn(false);

        String payload = """
                {
                  "email": "test@restaurant.com",
                  "password": "wrongPassword"
                }
                """;

        mockMvc.perform(post("/api/restaurants/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginReturnsBadRequestWhenEmailMissing() throws Exception {
        String payload = """
                {
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/restaurants/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginReturnsBadRequestWhenPasswordMissing() throws Exception {
        String payload = """
                {
                  "email": "test@restaurant.com"
                }
                """;

        mockMvc.perform(post("/api/restaurants/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginReturnsNotFoundWhenEmailDoesNotExist() throws Exception {
        when(restaurantService.findByEmail("notfound@restaurant.com"))
                .thenThrow(new ResponseStatusException(NOT_FOUND, "Restaurant not found"));

        String payload = """
                {
                  "email": "notfound@restaurant.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/restaurants/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isNotFound());
    }

    @Test
    void approveRestaurantReturnsOk() throws Exception {
        // According to SecurityConfig, POST /api/restaurants/** requires authentication
        // except for /onboard and /login. So this endpoint requires auth.
        when(jwtTokenProvider.validateToken("valid-token")).thenReturn(true);
        when(jwtTokenProvider.getRestaurantIdFromToken("valid-token")).thenReturn(1L);

        mockMvc.perform(post("/api/restaurants/1/approve")
                .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk());
    }

    @Test
    void publishMenuReturnsOk() throws Exception {
        // According to SecurityConfig, POST /api/restaurants/** requires authentication
        when(jwtTokenProvider.validateToken("valid-token")).thenReturn(true);
        when(jwtTokenProvider.getRestaurantIdFromToken("valid-token")).thenReturn(1L);

        mockMvc.perform(post("/api/restaurants/1/menu/publish")
                .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk());
    }

    @Test
    void createMenuReturnsUnauthorizedWhenNotAuthenticated() throws Exception {
        String payload = """
                {
                  "items": [
                    {
                      "name": "Pizza",
                      "description": "Delicious pizza",
                      "price": 12.99
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/restaurants/1/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createMenuReturnsBadRequestWhenItemsEmpty() throws Exception {
        when(jwtTokenProvider.validateToken("valid-token")).thenReturn(true);
        when(jwtTokenProvider.getRestaurantIdFromToken("valid-token")).thenReturn(1L);

        String payload = """
                {
                  "items": []
                }
                """;

        mockMvc.perform(post("/api/restaurants/1/menu")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }
}
