package com.mtogo.fulfillment.domain.port.out;

import com.mtogo.fulfillment.domain.model.FulfillmentOrder;
import java.util.Optional;

public interface FulfillmentRepositoryPort {
    FulfillmentOrder save(FulfillmentOrder order);
    Optional<FulfillmentOrder> findById(Long id);
    Optional<FulfillmentOrder> findByOrderId(Long orderId);
}
