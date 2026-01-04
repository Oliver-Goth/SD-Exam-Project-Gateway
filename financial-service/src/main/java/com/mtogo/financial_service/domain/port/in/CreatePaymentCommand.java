package com.mtogo.financial_service.domain.port.in;

import com.mtogo.financial_service.domain.model.Currency;

// DTO 
// A command object to encapsulate the data needed to create a payment
public class CreatePaymentCommand {

    public Long orderId;              
    public Double amount;           
    public Currency currency;  

    public CreatePaymentCommand() {
        // PMD suggestion to write comment
    }

    public CreatePaymentCommand(Long orderId, Double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }
}
