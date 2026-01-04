package com.mtogo.financial_service.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;

    private Long orderId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String paymentProvider;

    private String paymentProviderId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Commission> commissions = new ArrayList<>();

    public Payment() {}

    public Payment(Long orderId, Double amount, PaymentStatus status, String paymentProvider, String paymentProviderId) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.paymentProvider = paymentProvider;
        this.paymentProviderId = paymentProviderId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Payment(Long orderId, Double amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public String getPaymentProviderId() {
        return paymentProviderId;
    }

    public void setPaymentProviderId(String paymentProviderId) {
        this.paymentProviderId = paymentProviderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Commission> getCommissions() {
        return commissions;
    }

    // BI-DIRECTIONAL HELPERS   

    public void addCommission(Commission commission) {
        commissions.add(commission);
        commission.setPayment(this);
    }

    public void removeCommission(Commission commission) {
        commissions.remove(commission);
        commission.setPayment(null);
    }
}
