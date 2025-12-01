package com.mtogo.customer.infrastructure.adapter.out.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private final JwtTokenProvider provider = new JwtTokenProvider();

    @Test
    void generatesAndValidatesToken() {
        Long userId = 123L;

        String token = provider.generateToken(userId);
        Long parsed = provider.validateAndGetUserId(token);

        assertEquals(userId, parsed);
    }

    @Test
    void returnsNullForInvalidSignature() {
        String token = provider.generateToken(99L) + "tamper";

        assertNull(provider.validateAndGetUserId(token));
    }

    @Test
    void returnsNullForMalformedToken() {
        assertNull(provider.validateAndGetUserId("not-a-jwt"));
    }
}
