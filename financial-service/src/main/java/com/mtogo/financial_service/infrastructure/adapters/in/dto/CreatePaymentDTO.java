package com.mtogo.financial_service.infrastructure.adapters.in.dto;
import com.mtogo.financial_service.domain.model.Currency;   

public class CreatePaymentDTO {

    public Long orderId;
    public Double amount;
    public Currency currency;
    public String paymentProvider;
    public String paymentProviderId;

}

