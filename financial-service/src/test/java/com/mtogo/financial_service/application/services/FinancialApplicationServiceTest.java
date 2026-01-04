package com.mtogo.financial_service.application.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.mtogo.financial_service.application.exceptions.PaymentNotFoundException;
import com.mtogo.financial_service.domain.model.Commission;
import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.PaymentStatus;
import com.mtogo.financial_service.domain.port.in.CreatePaymentCommand;
import com.mtogo.financial_service.domain.port.out.CommissionEventPublisherPort;
import com.mtogo.financial_service.domain.port.out.CommissionRepositoryPort;
import com.mtogo.financial_service.domain.port.out.PaymentEventPublisherPort;
import com.mtogo.financial_service.domain.port.out.PaymentRepositoryPort;

class FinancialApplicationServiceTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @Mock
    private PaymentEventPublisherPort paymentEventPublisher;

    @Mock
    private CommissionRepositoryPort commissionRepository;

    @Mock
    private CommissionEventPublisherPort commissionEventPublisher;

    @InjectMocks
    private FinancialApplicationService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPayment_savesPaymentAndReturnsResult() {

        CreatePaymentCommand command =
                new CreatePaymentCommand(1L, 200.0);

        Payment savedPayment = new Payment(
                1L,
                200.0,
                PaymentStatus.COMPLETED,
                "PayPal",
                "paypal_txn_123"
        );
        savedPayment.setId(10L);

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(savedPayment);

        Payment result = service.createPayment(command);

        assertNotNull(result);
        assertEquals(200.0, result.getAmount());
        assertNotNull(result.getId());

        verify(paymentRepository, atLeastOnce())
                .save(any(Payment.class));
    }

    @Test
    void createPayment_whenCompleted_publishesPaymentEvent() {

        CreatePaymentCommand command =
                new CreatePaymentCommand(2L, 100.0);

        Payment completedPayment = new Payment(
                2L,
                100.0,
                PaymentStatus.COMPLETED,
                "PayPal",
                "paypal_txn_456"
        );
        completedPayment.setId(20L);

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(completedPayment);

        service.createPayment(command);

        verify(paymentEventPublisher, times(1))
                .publish(any());
    }

    @Test
    void createPayment_whenNotCompleted_doesNotPublishPaymentEvent() {

        CreatePaymentCommand command =
                new CreatePaymentCommand(3L, 150.0);

        Payment pendingPayment = new Payment(
                3L,
                150.0,
                PaymentStatus.PENDING,
                "PayPal",
                "paypal_txn_789"
        );
        pendingPayment.setId(30L);

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(pendingPayment);

        service.createPayment(command);

        verify(paymentEventPublisher, never())
                .publish(any());
    }

    @Test
    void getPayment_whenExists_returnsPayment() {

        Payment payment = new Payment(
                1L,
                100.0,
                PaymentStatus.COMPLETED,
                "PayPal",
                "txn"
        );
        payment.setId(1L);

        when(paymentRepository.findByPaymentId(1L))
                .thenReturn(Optional.of(payment));

        Payment result = service.getPayment(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getPayment_whenNotFound_throwsException() {

        when(paymentRepository.findByPaymentId(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                PaymentNotFoundException.class,
                () -> service.getPayment(99L)
        );
    }


    @Test
    void getCommissions_returnsCommissionList() {

        List<Commission> commissions = Collections.emptyList();

        when(commissionRepository.findByPaymentId(1L))
                .thenReturn(commissions);

        List<Commission> result = service.getCommissions(1L);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(commissionRepository, times(1))
                .findByPaymentId(1L);
    }
}
