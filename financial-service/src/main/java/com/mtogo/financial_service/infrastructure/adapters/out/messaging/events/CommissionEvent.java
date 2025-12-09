package com.mtogo.financial_service.infrastructure.adapters.out.messaging.events;

import java.util.List;

import com.mtogo.financial_service.domain.model.Commission;

public class CommissionEvent {

    private Long paymentId;
    private List<Commission> commissions;

    public CommissionEvent() {}

    public CommissionEvent(Long paymentId, List<Commission> commissions) {
        this.paymentId = paymentId;
        this.commissions = commissions;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public List<Commission> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<Commission> commissions) {
        this.commissions = commissions;
    }

    @Override
    public String toString() {
        return "CommissionEvent{" +
                "paymentId=" + paymentId +
                ", commissions=" + commissions +
                '}';
    }
}
