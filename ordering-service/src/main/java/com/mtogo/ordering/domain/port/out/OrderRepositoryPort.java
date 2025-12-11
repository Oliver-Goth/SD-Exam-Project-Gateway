package com.mtogo.ordering.domain.port.out;

import com.mtogo.ordering.domain.model.Order;
import java.util.Optional;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long id);
}
