package com.mtogo.restaurant.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderUtilTest {

    private PasswordEncoderUtil passwordEncoderUtil;

    @BeforeEach
    void setUp() {
        passwordEncoderUtil = new PasswordEncoderUtil();
    }

    @Test
    void encodeReturnsNonNullHashedPassword() {
        String rawPassword = "myPassword123";

        String encoded = passwordEncoderUtil.encode(rawPassword);

        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
    }

    @Test
    void encodeReturnsDifferentHashForSamePasswordDueToSalt() {
        String rawPassword = "myPassword123";

        String encoded1 = passwordEncoderUtil.encode(rawPassword);
        String encoded2 = passwordEncoderUtil.encode(rawPassword);

        assertNotEquals(encoded1, encoded2);
    }

    @Test
    void matchesReturnsTrueWhenPasswordMatches() {
        String rawPassword = "myPassword123";
        String encoded = passwordEncoderUtil.encode(rawPassword);

        boolean matches = passwordEncoderUtil.matches(rawPassword, encoded);

        assertTrue(matches);
    }

    @Test
    void matchesReturnsFalseWhenPasswordDoesNotMatch() {
        String rawPassword = "myPassword123";
        String encoded = passwordEncoderUtil.encode(rawPassword);

        boolean matches = passwordEncoderUtil.matches("wrongPassword", encoded);

        assertFalse(matches);
    }

    @Test
    void encodedPasswordIsNotPlainText() {
        String rawPassword = "secret";

        String encoded = passwordEncoderUtil.encode(rawPassword);

        assertFalse(encoded.equals(rawPassword));
        assertTrue(encoded.length() > rawPassword.length());
    }
}
