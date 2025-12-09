package com.mtogo.financial_service.domain.port.in;
import com.mtogo.financial_service.domain.model.Payment;

public interface CreatePaymentUseCase {
    
    Payment createPayment(CreatePaymentCommand command);
    
}
