package com.mtogo.financial_service.domain.port.in;

import com.mtogo.financial_service.domain.model.Payment;

public interface GetPaymentUseCase {

    Payment getPayment(Long id);
}
