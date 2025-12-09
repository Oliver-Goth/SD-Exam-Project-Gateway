package com.mtogo.financial_service.domain.port.out;

import java.util.List;

import com.mtogo.financial_service.domain.model.Commission;

public interface CommissionRepositoryPort {

    List<Commission> findByPaymentId(Long paymentId);
    
}
