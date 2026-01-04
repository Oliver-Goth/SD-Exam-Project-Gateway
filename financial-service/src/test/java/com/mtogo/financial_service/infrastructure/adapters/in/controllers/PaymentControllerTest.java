package com.mtogo.financial_service.infrastructure.adapters.in.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.mtogo.financial_service.domain.model.Commission;
import com.mtogo.financial_service.domain.model.Payment;
import com.mtogo.financial_service.domain.model.PaymentStatus;
import com.mtogo.financial_service.domain.model.Role;
import com.mtogo.financial_service.domain.port.in.CreatePaymentUseCase;
import com.mtogo.financial_service.domain.port.in.GetCommissionsUseCase;
import com.mtogo.financial_service.domain.port.in.GetPaymentUseCase;
import com.mtogo.financial_service.domain.port.in.PaymentStatusUseCase;

@WebMvcTest(controllers = PaymentController.class)
@AutoConfigureMockMvc(addFilters = false) 
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // All use cases are mocked
    @MockBean
    private CreatePaymentUseCase createPaymentUseCase;

    @MockBean
    private GetPaymentUseCase getPaymentUseCase;

    @MockBean
    private PaymentStatusUseCase paymentStatusUseCase;

    @MockBean
    private GetCommissionsUseCase getCommissionsUseCase;

    @Test
    void createPaymentTest() throws Exception {

        Payment payment = new Payment(
                1L,
                250.0,
                PaymentStatus.COMPLETED,
                "TEST",
                "TEST_ID"
        );
        payment.setId(10L);

        when(createPaymentUseCase.createPayment(ArgumentMatchers.any()))
                .thenReturn(payment);

        String payload = """
                
        {
                  "orderId": 1,
                  "amount": 250.0,
                  "currency": "DKK",
                  "paymentProvider": "TEST",
                  "paymentProviderId": "TEST_ID"
                }

                """

        ;

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(10L))
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.amount").value(250.0))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.paymentProvider").value("TEST"));
    }

    @Test
    void getPaymentTest() throws Exception {

        Payment payment = new Payment(
                1L,
                150.0,
                PaymentStatus.PENDING,
                "TEST",
                "TEST"
        );
        payment.setId(20L);

        when(getPaymentUseCase.getPayment(1L))
                .thenReturn(payment);

        mockMvc.perform(get("/payments/{orderId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(20L))
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getPaymentStatusFailedTest() throws Exception {

        Payment payment = new Payment(
                1L,
                99.0,
                PaymentStatus.FAILED,
                "TEST",
                "TEST"
        );
        payment.setId(30L);

        when(paymentStatusUseCase.getPaymentStatus(1L))
                .thenReturn(payment);

        mockMvc.perform(get("/payments/{paymentId}/status", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(30L))
                .andExpect(jsonPath("$.status").value("FAILED"));
    }

    @Test
    void getCommissionsTest() throws Exception {

        Payment payment = new Payment(
                1L,
                100.0,
                PaymentStatus.COMPLETED,
                "TEST",
                "TEST"
        );
       
        payment.setId(99L);

        Commission commission = new Commission(
                payment,
                Role.MTOGO,
                6
        );

        List<Commission> commissions = Collections.singletonList(commission);

        when(getCommissionsUseCase.getCommissions(1L)).thenReturn(commissions);

        mockMvc.perform(get("/payments/commissions/{paymentId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentId").value(99L))
                .andExpect(jsonPath("$[0].role").value("MTOGO"))
                .andExpect(jsonPath("$[0].amount").value(6));
                
    }
}
