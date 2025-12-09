package com.mtogo.financial_service.infrastructure.adapters.out.messaging.events;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.mtogo.financial_service.domain.model.Payment;

public class PaymentEvent implements Serializable {

    final private Long paymentId;
    final private Long orderId;
    final private Double amount;
    final private PaymentEventType type;
    final private LocalDateTime timestamp;

    public PaymentEvent(Long paymentId, Long orderId, Double amount, PaymentEventType type) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public static PaymentEvent created(Payment payment) {
        return new PaymentEvent(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                PaymentEventType.CREATED);
    }

    public static PaymentEvent confirmed(Payment payment) {
        return new PaymentEvent(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                PaymentEventType.CONFIRMED);
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public PaymentEventType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }   

    @Override
    public String toString() {
        return "PaymentEvent{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", type=" + type +
                ", timestamp=" + timestamp +
                '}';
    }
}
