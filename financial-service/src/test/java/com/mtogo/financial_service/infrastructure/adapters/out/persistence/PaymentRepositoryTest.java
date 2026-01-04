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
                1001L,
                250.0,
                PaymentStatus.COMPLETED,
                "TestProvider",
                "TEEEEEST"
        );
        payment.setCurrency(Currency.DKK);

        Payment saved = paymentRepository.save(payment);

        assertNotNull(saved.getId());

        Optional<Payment> found = paymentRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(1001L, found.get().getOrderId());
        assertEquals(PaymentStatus.COMPLETED, found.get().getStatus());
        assertEquals(Currency.DKK, found.get().getCurrency());
        assertEquals(found.get().getPaymentProviderId(), "TEEEEEST");
    }

    @Test
    void testFindByOrderId() {
        Payment payment = new Payment(
                2002L,
                99.99,
                PaymentStatus.PENDING,
                "TestProviderTest",
                "test123"
        );
        payment.setCurrency(Currency.EUR);

        paymentRepository.save(payment);

        Optional<Payment> found = paymentRepository.findByOrderId(2002L);
        assertTrue(found.isPresent());
        assertEquals(99.99, found.get().getAmount());
        assertEquals(PaymentStatus.PENDING, found.get().getStatus());
        assertEquals(Currency.EUR, found.get().getCurrency());
        assertEquals("test123", found.get().getPaymentProviderId());
    }

    @Test
    void testFindAllByOrderId() {
        Payment p1 = new Payment(
                3003L,
                50.0,
                PaymentStatus.COMPLETED,
                "TestProvider",
                "test_1"
        );
        Payment p2 = new Payment(
                3003L,
                75.0,
                PaymentStatus.FAILED,
                "TestProvider",
                "test_2"
        );

        p1.setCurrency(Currency.EUR);
        p2.setCurrency(Currency.EUR);

        paymentRepository.save(p1);
        paymentRepository.save(p2);

        List<Payment> payments = paymentRepository.findAllByOrderId(3003L);
        assertEquals(2, payments.size());
    }

    @Test
    void testDeletePayment() {
        Payment payment = new Payment(
                4004L,
                120.0,
                PaymentStatus.COMPLETED,
                "PayPal",
                "test_delete"
        );
        payment.setCurrency(Currency.EUR);

        Payment saved = paymentRepository.save(payment);
        assertNotNull(saved.getId());

        paymentRepository.delete(saved);

        assertFalse(paymentRepository.findById(saved.getId()).isPresent());
    }
}
