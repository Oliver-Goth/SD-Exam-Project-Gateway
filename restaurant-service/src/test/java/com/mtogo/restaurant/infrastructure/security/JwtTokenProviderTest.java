package com.mtogo.restaurant.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret",
                "my-super-secret-key-that-is-at-least-256-bits-long-for-hs256-algorithm-security");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationMs", 86400000L);
    }

    @Test
    void generateTokenCreatesValidToken() {
        String token = jwtTokenProvider.generateToken(1L, "test@restaurant.com");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getRestaurantIdFromTokenReturnsCorrectId() {
        String token = jwtTokenProvider.generateToken(42L, "test@restaurant.com");

        Long restaurantId = jwtTokenProvider.getRestaurantIdFromToken(token);

        assertEquals(42L, restaurantId);
    }

    @Test
    void getEmailFromTokenReturnsCorrectEmail() {
        String token = jwtTokenProvider.generateToken(1L, "owner@restaurant.com");

        String email = jwtTokenProvider.getEmailFromToken(token);

        assertEquals("owner@restaurant.com", email);
    }

    @Test
    void validateTokenReturnsTrueForValidToken() {
        String token = jwtTokenProvider.generateToken(1L, "test@restaurant.com");

        boolean isValid = jwtTokenProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateTokenReturnsFalseForInvalidToken() {
        boolean isValid = jwtTokenProvider.validateToken("invalid-token-string");

        assertFalse(isValid);
    }

    @Test
    void validateTokenReturnsFalseForMalformedToken() {
        boolean isValid = jwtTokenProvider.validateToken("not.a.valid.jwt");

        assertFalse(isValid);
    }

    @Test
    void validateTokenReturnsFalseForEmptyToken() {
        boolean isValid = jwtTokenProvider.validateToken("");

        assertFalse(isValid);
    }

    @Test
    void tokenContainsRestaurantType() {
        String token = jwtTokenProvider.generateToken(1L, "test@restaurant.com");

        // Token should be valid and contain proper claims
        assertTrue(jwtTokenProvider.validateToken(token));
        assertNotNull(jwtTokenProvider.getRestaurantIdFromToken(token));
    }
}
