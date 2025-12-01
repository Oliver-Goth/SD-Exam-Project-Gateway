package com.mtogo.customer.infrastructure.adapter.out.messaging;

import com.mtogo.customer.domain.port.out.EmailVerificationPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class EmailVerificationAdapter implements EmailVerificationPort {

    @Override
    public void sendVerification(String email, String token) {
        System.out.println("===== MOCK EMAIL SENT =====");
        System.out.println("To: " + email);
        System.out.println("Verification link:");
        System.out.println("http://localhost:8081/auth/verify?token=" + token);
    }
}
