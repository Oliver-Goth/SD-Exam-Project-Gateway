package com.mtogo.ordering.domain.port.out;

import com.mtogo.ordering.domain.model.Order;

public interface OrderEventPublisherPort {
    void publishOrderConfirmed(Order order);
}
