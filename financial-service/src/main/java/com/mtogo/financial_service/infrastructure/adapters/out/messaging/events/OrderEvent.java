package com.mtogo.financial_service.infrastructure.adapters.out.messaging.events;

import java.math.BigDecimal;

public class OrderEvent {

    private Long orderId;
    private Long customerId;
    private BigDecimal total;
    private String currency;

    public OrderEvent() {
        // PMD suggestion to write comment
    } 

    public OrderEvent(Long orderId, Long customerId, BigDecimal total, String currency) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.total = total;
        this.currency = currency;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // GetMessage method for RabbitListener
    public String getMessage() {
        return "Order ID: " + orderId + ", Customer ID: " + customerId + ", Total: " + total + ", Currency: " + currency;
    }
}

