package com.mtogo.fulfillment.domain.port.in;

public interface MarkFulfillmentPreparedUseCase {
    void markPrepared(Long orderId);
}
