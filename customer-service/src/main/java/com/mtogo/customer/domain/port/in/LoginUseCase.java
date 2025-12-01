package com.mtogo.customer.domain.port.in;

public interface LoginUseCase {
    String login(String email, String rawPassword);
}
