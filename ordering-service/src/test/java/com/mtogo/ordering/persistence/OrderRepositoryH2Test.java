package com.mtogo.ordering.persistence;

import com.mtogo.ordering.domain.model.OrderStatus;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderJpaEntity;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderJpaRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryH2Test {

    @Autowired
    private OrderJpaRepository repo;

    @Test
    void testSaveOrder() {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setCustomerId(3L);
        entity.setRestaurantId(5L);
        entity.setTotal(new BigDecimal("29.99"));
        entity.setStatus(OrderStatus.CREATED);

        OrderJpaEntity saved = repo.save(entity);

        assertThat(saved.getId()).isNotNull();
    }
}
