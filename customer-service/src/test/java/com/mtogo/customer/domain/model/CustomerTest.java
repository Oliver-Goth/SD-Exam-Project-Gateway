package com.mtogo.customer.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {

    @Test
    void verifyAccountSetsStatusToVerified() {
        Customer customer = new Customer(
                1L,
                "Test User",
                "test@example.com",
                "hashed",
                AccountStatus.ACTIVE,
                VerificationStatus.PENDING,
                new ContactInfo("test@example.com", "12345678"),
                new Address("Street", "City", "0000"),
                Collections.emptyList()
        );

        customer.verifyAccount();

        assertEquals(VerificationStatus.VERIFIED, customer.getVerificationStatus());
    }
}
