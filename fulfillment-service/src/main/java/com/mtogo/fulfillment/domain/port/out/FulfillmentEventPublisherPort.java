package com.mtogo.fulfillment.domain.port.out;

import com.mtogo.fulfillment.domain.model.FulfillmentOrder;

public interface FulfillmentEventPublisherPort {
    void publishOrderPrepared(FulfillmentOrder order);
}
