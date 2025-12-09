package com.mtogo.financial_service.infrastructure.adapters.in.dto;
import com.mtogo.financial_service.domain.model.Payment;    
import com.mtogo.financial_service.domain.model.PaymentStatus;

public class PaymentResponseDTO {
    public Long paymentId;
    public Long orderId;
    public Double amount;
    public PaymentStatus status;
    public String paymentProvider;

    public PaymentResponseDTO(Payment payment) {
        this.paymentId = payment.getId();
        this.orderId = payment.getOrderId();
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.paymentProvider = payment.getPaymentProvider();
    }
}
