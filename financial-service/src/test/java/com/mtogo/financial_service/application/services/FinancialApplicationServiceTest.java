package com.mtogo.financial_service.application.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.mtogo.financial_service.application.exceptions.PaymentNotCreatedException;
import com.mtogo.financial_service.application.exceptions.PaymentNotFoundException;
import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.PaymentStatus;
import com.mtogo.financial_service.domain.port.in.ConfirmPaymentProvider;
import com.mtogo.financial_service.domain.port.in.CreatePaymentCommand;
import com.mtogo.financial_service.domain.port.out.PaymentEventPublisherPort;
import com.mtogo.financial_service.domain.port.out.PaymentRepositoryPort;
import com.mtogo.financial_service.application.exceptions.PaymentStatusNotFoundException;

class FinancialApplicationServiceTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;
    @Mock
    private PaymentEventPublisherPort paymentEventPublisher;
    @Mock
    private ConfirmPaymentProvider paymentProvider;
    @InjectMocks
    private FinancialApplicationService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void TestCreatePayment() {

        CreatePaymentCommand command = new CreatePaymentCommand(1L, 200.0);

        doAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setStatus(PaymentStatus.COMPLETED);
            p.setPaymentProvider("TEST");
            p.setPaymentProviderId("TEST123");
            return null;
        }).when(paymentProvider).processPayment(any(Payment.class));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> {
                    Payment p = invocation.getArgument(0);
                    p.setId(10L);
                    return p;
                });

        Payment payment = service.createPayment(command);

        assertEquals("TEST", payment.getPaymentProvider());
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertNotNull(payment.getId());
    }

    @Test
    void TestPublishPaymentEvent() {

        CreatePaymentCommand command = new CreatePaymentCommand(2L, 100.0);

        doAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setStatus(PaymentStatus.COMPLETED);
            return null;
        }).when(paymentProvider).processPayment(any(Payment.class));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> {
                    Payment p = invocation.getArgument(0);
                    p.setId(20L);
                    return p;
                });

        service.createPayment(command);

        verify(paymentEventPublisher, times(1)).publish(any());
    }

    @Test
    void TestDoesNotPublishPaymentEvent() {

        CreatePaymentCommand command = new CreatePaymentCommand(3L, 150.0);

        doAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setStatus(PaymentStatus.PENDING);
            return null;
        }).when(paymentProvider).processPayment(any(Payment.class));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> {
                    Payment p = invocation.getArgument(0);
                    p.setId(30L);
                    return p;
                });

        service.createPayment(command);

        verify(paymentEventPublisher, never()).publish(any());
    }

    @Test
    void TestGetPaymentSuccess() {

        Payment payment = new Payment(
                1L,
                100.0,
                PaymentStatus.COMPLETED,
                "TEST",
                "TEST_ID"
        );
        payment.setId(1L);

        when(paymentRepository.findByPaymentId(1L))
                .thenReturn(Optional.of(payment));

        Payment result = service.getPayment(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void TestGetPaymentFailure() {

        when(paymentRepository.findByPaymentId(80L)).thenReturn(Optional.empty());

        assertThrows(
                PaymentNotFoundException.class,
                () -> service.getPayment(80L)
        );
    }

    @Test
    void TestGetPaymentStatusSuccess() {

        Payment payment = new Payment(
                2L,
                80.0,
                PaymentStatus.COMPLETED,
                "TEST",
                "TEST"
        );
        payment.setId(2L);

        when(paymentRepository.getStatus(2L))
                .thenReturn(Optional.of(payment));

        Payment result = service.getPaymentStatus(2L);

        assertNotNull(result);
        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
    }

    @Test
    void TestGetPaymentStatusFailure() {

        when(paymentRepository.getStatus(85L)).thenReturn(Optional.empty());

        assertThrows(
                PaymentStatusNotFoundException.class,
                () -> service.getPaymentStatus(85L)
        );
    }

    @Test
    void createPaymentWithoutID() {

        CreatePaymentCommand command = new CreatePaymentCommand(1L, 100.0);

        Payment paymentWithoutId = new Payment(1L, 100.0, PaymentStatus.COMPLETED, "TEST", "TEST_ID");
        
        paymentWithoutId.setId(null); 

        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentWithoutId);

        assertThrows(
                PaymentNotCreatedException.class,
                () -> service.createPayment(command)
        );
    }

}
