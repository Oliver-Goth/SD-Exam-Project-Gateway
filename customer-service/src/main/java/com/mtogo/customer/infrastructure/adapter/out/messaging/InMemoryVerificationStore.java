package com.mtogo.customer.infrastructure.adapter.out.messaging;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryVerificationStore {

    private final Map<Long, String> tokenStore = new ConcurrentHashMap<>();

    public void saveToken(Long customerId, String token) {
        tokenStore.put(customerId, token);
    }

    public String getToken(Long customerId) {
        return tokenStore.get(customerId);
    }

    public void removeToken(Long customerId) {
        tokenStore.remove(customerId);
    }
}
