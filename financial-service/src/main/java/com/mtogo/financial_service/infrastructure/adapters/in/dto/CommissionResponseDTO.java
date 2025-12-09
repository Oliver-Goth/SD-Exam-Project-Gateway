package com.mtogo.financial_service.infrastructure.adapters.in.dto;

import com.mtogo.financial_service.domain.model.Commission;
import com.mtogo.financial_service.domain.model.Role;

public class CommissionResponseDTO {

    public Long commissionId;
    public Long paymentId;
    public Role role;
    public double amount;

    public CommissionResponseDTO(Commission commission) {
        this.commissionId = commission.getId();
        this.paymentId = commission.getPayment().getId();
        this.role = commission.getRole();
        this.amount = commission.getAmount();
    }
}
