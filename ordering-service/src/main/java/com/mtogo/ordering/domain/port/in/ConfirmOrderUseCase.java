package com.mtogo.ordering.domain.port.in;

import com.mtogo.ordering.domain.model.Order;

public interface ConfirmOrderUseCase {

    Order confirmOrder(Long orderId);
}
