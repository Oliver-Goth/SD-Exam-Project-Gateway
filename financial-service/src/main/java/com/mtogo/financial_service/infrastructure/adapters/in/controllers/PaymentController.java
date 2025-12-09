package com.mtogo.financial_service.infrastructure.adapters.in.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtogo.financial_service.domain.model.Commission;
import com.mtogo.financial_service.domain.model.Payment;    
import com.mtogo.financial_service.domain.port.in.CreatePaymentCommand;
import com.mtogo.financial_service.domain.port.in.CreatePaymentUseCase;
import com.mtogo.financial_service.domain.port.in.GetPaymentUseCase;
import com.mtogo.financial_service.domain.port.in.PaymentStatusUseCase;
import com.mtogo.financial_service.infrastructure.adapters.in.dto.CreatePaymentDTO;
import com.mtogo.financial_service.infrastructure.adapters.in.dto.PaymentResponseDTO;
import com.mtogo.financial_service.domain.port.in.GetCommissionsUseCase;
import com.mtogo.financial_service.infrastructure.adapters.in.dto.CommissionResponseDTO;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final CreatePaymentUseCase createPaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;
    private final PaymentStatusUseCase paymentStatusUseCase;
    private final GetCommissionsUseCase getCommissionsUseCase;

    public PaymentController(CreatePaymentUseCase createPaymentUseCase,
                             GetPaymentUseCase getPaymentUseCase, 
                             PaymentStatusUseCase paymentStatusUseCase,
                             GetCommissionsUseCase getCommissionsUseCase) {
        this.createPaymentUseCase = createPaymentUseCase;
        this.getPaymentUseCase = getPaymentUseCase;
        this.paymentStatusUseCase = paymentStatusUseCase; 
        this.getCommissionsUseCase = getCommissionsUseCase;  
    }

    @PostMapping
    public PaymentResponseDTO createPayment(@RequestBody CreatePaymentDTO dto) {
        CreatePaymentCommand command = new CreatePaymentCommand();
        command.orderId = dto.orderId;
        command.amount = dto.amount;
        command.currency = dto.currency;
        command.paymentProvider = dto.paymentProvider;
        command.paymentProviderId = dto.paymentProviderId;

        Payment payment = createPaymentUseCase.createPayment(command);
        return new PaymentResponseDTO(payment);
    }

    @GetMapping("/{orderId}")
    public PaymentResponseDTO getPayment(@PathVariable Long orderId) {
        Payment payment = getPaymentUseCase.getPayment(orderId);
        return new PaymentResponseDTO(payment);
    }
    
    @GetMapping("/{orderId}/status")
    public PaymentResponseDTO getStatusPayment(@PathVariable Long paymentId) {
        Payment payment = paymentStatusUseCase.getPaymentStatus(paymentId);
        return new PaymentResponseDTO(payment);
    }

    @GetMapping("/commissions/{paymentId}")
    public List<CommissionResponseDTO> getCommissions(@PathVariable("paymentId") Long paymentId) {
        List<Commission> commissions = getCommissionsUseCase.getCommissions(paymentId);
        return commissions.stream()
                        .map(CommissionResponseDTO::new)
                        .toList();
    }
}
