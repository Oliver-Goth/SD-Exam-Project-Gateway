package com.mtogo.financial_service.domain.port.out;

import com.mtogo.financial_service.infrastructure.adapters.out.messaging.events.PaymentEvent;

public interface PaymentEventPublisherPort {
    
    void publish(PaymentEvent event);

}