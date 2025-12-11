package com.mtogo.ordering.domain.port.in;

import com.mtogo.ordering.domain.model.Order;

public interface GetOrderUseCase {

    Order getOrder(Long orderId);
}
