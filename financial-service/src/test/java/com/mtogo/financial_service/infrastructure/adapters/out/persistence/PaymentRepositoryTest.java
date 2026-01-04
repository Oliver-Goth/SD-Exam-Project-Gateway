package com.mtogo.financial_service.infrastructure.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mtogo.financial_service.domain.model.Currency;
import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.PaymentStatus;

@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("payment_test_db")
                    .withUsername("testusername")
                    .withPassword("testpassword");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private PaymentJpaRepository paymentRepository;

    @Test
    void testSaveAndFindPayment() {
        Payment payment = new Payment(
                1L,
                250.0,
                PaymentStatus.COMPLETED,
                "TEST",
                "TEEEEEST"
        );
        payment.setCurrency(Currency.DKK);

        Payment saved = paymentRepository.save(payment);

        assertNotNull(saved.getId());

        Optional<Payment> found = paymentRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getOrderId());
        assertEquals(PaymentStatus.COMPLETED, found.get().getStatus());
        assertEquals(Currency.DKK, found.get().getCurrency());
        assertEquals(found.get().getPaymentProviderId(), "TEEEEEST");
        assertEquals(250.0, found.get().getAmount());
    }

    @Test
    void testFindByOrderId() {
        Payment payment = new Payment(
                2L,
                99.99,
                PaymentStatus.PENDING,
                "TEST",
                "test123456"
        );
        payment.setCurrency(Currency.EUR);

        paymentRepository.save(payment);

        Optional<Payment> found = paymentRepository.findByOrderId(2L);
        assertTrue(found.isPresent());
        assertEquals(99.99, found.get().getAmount());
        assertEquals(PaymentStatus.PENDING, found.get().getStatus());
        assertEquals(Currency.EUR, found.get().getCurrency());
        assertEquals("test123456", found.get().getPaymentProviderId());
    }

    @Test
    void testDeletePayment() {
        Payment payment = new Payment(
                4L,
                120.0,
                PaymentStatus.COMPLETED,
                "PROVIDERTEST",
                "DELETE_TEST"
        );
        payment.setCurrency(Currency.DKK);

        Payment saved = paymentRepository.save(payment);
        assertNotNull(saved.getId());

        paymentRepository.delete(saved);

        assertFalse(paymentRepository.findById(saved.getId()).isPresent());
    
    }
}
