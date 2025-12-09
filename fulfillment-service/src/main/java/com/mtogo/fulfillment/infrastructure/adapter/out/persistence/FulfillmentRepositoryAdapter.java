package com.mtogo.fulfillment.infrastructure.adapter.out.persistence;

import com.mtogo.fulfillment.domain.model.FulfillmentOrder;
import com.mtogo.fulfillment.domain.port.out.FulfillmentRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FulfillmentRepositoryAdapter implements FulfillmentRepositoryPort {

    private final FulfillmentJpaRepository jpaRepository;
    private final FulfillmentMapper mapper;

    public FulfillmentRepositoryAdapter(FulfillmentJpaRepository jpaRepository, FulfillmentMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public FulfillmentOrder save(FulfillmentOrder order) {
        FulfillmentJpaEntity entity = mapper.toEntity(order);
        FulfillmentJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<FulfillmentOrder> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<FulfillmentOrder> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).map(mapper::toDomain);
    }
}
