package com.mtogo.customer.domain.port.out;

public interface EmailVerificationPort {
    void sendVerification(String email, String token);
}
