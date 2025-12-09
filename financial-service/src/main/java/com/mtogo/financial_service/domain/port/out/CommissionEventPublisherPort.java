package com.mtogo.financial_service.domain.port.out;

import com.mtogo.financial_service.infrastructure.adapters.out.messaging.events.CommissionEvent;

public interface CommissionEventPublisherPort {
    
    void publish(CommissionEvent event);

}