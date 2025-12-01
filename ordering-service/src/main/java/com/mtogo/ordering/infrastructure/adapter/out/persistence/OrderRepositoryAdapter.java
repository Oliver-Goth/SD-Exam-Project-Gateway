package com.mtogo.ordering.infrastructure.adapter.out.persistence;

import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.port.out.OrderRepositoryPort;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository repository;
    private final OrderMapper mapper = new OrderMapper();

    public OrderRepositoryAdapter(OrderJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = mapper.toEntity(order);
        OrderJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
}
