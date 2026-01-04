package com.mtogo.financial_service.application.services.helpers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mtogo.financial_service.domain.model.Payment;

public class CommissionCalculatorTest {


    // Helper method to create Payment instances for testing purposes
    private Payment createPayment(double amount, LocalDateTime createdAt) {
        
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setCreatedAt(createdAt);
        return payment;

    }

    // Equivalence Partitioning Tests (the four defined partitions for the MTOGO role fee)

    @Test
    @DisplayName("EP1: Order amount that are <= 100 --> MTOGO should have 6% fee")
    void testMtogoFee_LowOrderAmount() {
        Payment payment = createPayment(50.0, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream() // stream to filter for MTOGO commission
                .filter(c -> c.getRole().name().equals("MTOGO")) // filter for MTOGO role as this is where the fee logic is calculated
                .findFirst()
                .get()
                .getAmount(); // Retrieve the fee amount

        assertEquals(3.0, fee); // manually calculated 6% of 50
    }

    @Test
    @DisplayName("EP2: Order amount between 100 and 500 --> MTOGO should have a 5% fee")
    void testMtogoFee_MediumOrderAmount() {
        Payment payment = createPayment(200.0, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream()
                .filter(c -> c.getRole().name().equals("MTOGO"))
                .findFirst()
                .get()
                .getAmount();

        assertEquals(11.0, fee);
    }

    @Test
    @DisplayName("EP3: Order amount above 1000 --> MTOGO should have a 3% fee")
    void testMtogoFee_LargeOrderAmount() {
        Payment payment = createPayment(1500.0, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream()
                .filter(c -> c.getRole().name().equals("MTOGO"))
                .findFirst()
                .get()
                .getAmount();

        assertEquals(61.0, fee);
    }

    @Test
    @DisplayName("EP4: Order amount between 500 and 1000 --> MTOGO should have a 4% fee")
    void testMtogoFee_MediumHighOrderAmount() {
        Payment payment = createPayment(750.0, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream()
                .filter(c -> c.getRole().name().equals("MTOGO"))
                .findFirst()
                .get()
                .getAmount();

        assertEquals(36.0, fee);
    }

    // Other EP Tests

    @Test
    @DisplayName("EP: Delivery commission before 8 PM")
    void testDeliveryCommission_dayTime() {
        Payment payment = createPayment(
                100.0,
                LocalDateTime.of(2024, 6, 10, 18, 0)
        );

        double commission = CommissionCalculator
                .calculateDeliveryAgentCommission(payment, 100.0);

        assertEquals(5.0, commission, 0.01);
    }

    @Test
    @DisplayName("EP: Delivery commission after 8 PM")
    void testDeliveryCommission_nightTime() {
        Payment payment = createPayment(
                100.0,
                LocalDateTime.of(2024, 6, 10, 21, 0)
        );

        double commission = CommissionCalculator
                .calculateDeliveryAgentCommission(payment, 100.0);

        assertEquals(10.0, commission, 0.01);
    }


    // Boundary Value Analysis Tests

    @Test
    @DisplayName("Edge case: Order amount is exactly 100")
    void testBoundary_OrderAmount_100() {
        Payment payment = createPayment(100.0, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream()
                .filter(c -> c.getRole().name().equals("MTOGO"))
                .findFirst()
                .get()
                .getAmount();

        assertEquals(6.0, fee);
    }

    @Test
    @DisplayName("Edge case: Order amount just above 100")
    void testBoundary_OrderAmount_101() {
        Payment payment = createPayment(101.0, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream()
                .filter(c -> c.getRole().name().equals("MTOGO"))
                .findFirst()
                .get()
                .getAmount();

        assertEquals(6.05, fee);
    }
    
    @Test
    @DisplayName("Edge case: Order amount just below 100")
    void testBoundary_OrderAmount_99() {
        Payment payment = createPayment(99.9, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream()
                .filter(c -> c.getRole().name().equals("MTOGO"))
                .findFirst()
                .get()
                .getAmount();

        assertEquals(5.9, fee, 0.1);
    }


    @Test 
    @DisplayName("Edge case: Order amount is below 500")
    void testBoundary_OrderAmount_499() {
        Payment payment = createPayment(499.9, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream()
                .filter(c -> c.getRole().name().equals("MTOGO"))
                .findFirst()
                .get()
                .getAmount();

        assertEquals(25.9, fee, 0.1);
    }


    @Test 
    @DisplayName("Edge case: Order amount is just above 500")
    void testBoundary_OrderAmount_501() {
        Payment payment = createPayment(500.5, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream()
                .filter(c -> c.getRole().name().equals("MTOGO"))
                .findFirst()
                .get()
                .getAmount();

        assertEquals(26.02, fee, 0.1);
    }

    @Test 
    @DisplayName("Edge case: Order amount exactly 500")
    void testBoundary_OrderAmount_500() {
        Payment payment = createPayment(500.0, LocalDateTime.now());

        double fee = CommissionCalculator.calculateFor(payment)
                .stream()
                .filter(c -> c.getRole().name().equals("MTOGO"))
                .findFirst()
                .get()
                .getAmount();

        assertEquals(26.0, fee);
    }


    // Edge cases relating time

    @Test
    @DisplayName("Edge case: Delivery commission exactly at 8 PM")
    void testDeliveryCommission_AtBoundary() {
        Payment payment = createPayment(
                100.0,
                LocalDateTime.of(2024, 6, 10, 20, 0)
        );

        double commission = CommissionCalculator
                .calculateDeliveryAgentCommission(payment, 100.0);

        assertEquals(10.0, commission, 0.01);
    }

    @Test
    @DisplayName("Edge case: Holiday bonus on Christmas")
    void testDeliveryCommission_HolidayBonus() {
        Payment payment = createPayment(
                100.0,
                LocalDateTime.of(2024, 12, 24, 18, 0)
        );

        double commission = CommissionCalculator
                .calculateDeliveryAgentCommission(payment, 100.0);

        assertEquals(10.0, commission, 0.01);
    }

}
