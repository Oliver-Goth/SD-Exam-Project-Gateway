package com.mtogo.customer.domain.port.out;

public interface TokenProviderPort {

    String generateToken(Long userId);

    Long validateAndGetUserId(String token);
}
