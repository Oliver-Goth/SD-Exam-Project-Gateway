package com.mtogo.customer.infrastructure.adapter.out.security;

import com.mtogo.customer.domain.port.out.TokenProviderPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenProviderPort {

    private final Key key;
    private final long expiration;

    public JwtTokenProvider() {
        // In a production system, inject the secret via configuration
        this.key = Keys.hmacShaKeyFor("my_super_secret_key_1234567890123456".getBytes(StandardCharsets.UTF_8));
        this.expiration = 3_600_000; // 1 hour in milliseconds
    }

    @Override
    public String generateToken(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Long validateAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.getSubject());

        } catch (Exception e) {
            return null; // invalid token
        }
    }
}
