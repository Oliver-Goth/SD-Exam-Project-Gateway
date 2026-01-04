package com.mtogo.ordering.adapter;

import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import com.mtogo.ordering.domain.model.OrderStatus;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderJpaEntity;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderJpaRepository;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderMapper;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class OrderRepositoryAdapterTest {

    private OrderJpaRepository jpaRepository;
    private OrderRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        jpaRepository = Mockito.mock(OrderJpaRepository.class);
        adapter = new OrderRepositoryAdapter(jpaRepository);
    }

    @Test
    void testSaveOrder() {
        Order order = new Order(
                10L,
                20L,
                List.of(new OrderItem(100L, "Burger", new BigDecimal("9.99"), 2)));

        Mockito.when(jpaRepository.save(any(OrderJpaEntity.class)))
                .thenAnswer(invocation -> {
                    OrderJpaEntity entity = invocation.getArgument(0);
                    entity.setId(1L);
                    return entity;
                });

        Order saved = adapter.save(order);

        assertEquals(1L, saved.getId());
        assertEquals(10L, saved.getCustomerId());
        assertEquals(20L, saved.getRestaurantId());
        Mockito.verify(jpaRepository).save(any(OrderJpaEntity.class));
    }

    @Test
    void testSaveOrderWithExistingId() {
        Order order = new Order(
                10L,
                20L,
                List.of(new OrderItem(100L, "Burger", new BigDecimal("9.99"), 2)));
        order.setId(5L);

        Mockito.when(jpaRepository.save(any(OrderJpaEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order saved = adapter.save(order);

        assertEquals(5L, saved.getId());
        Mockito.verify(jpaRepository).save(any(OrderJpaEntity.class));
    }

    @Test
    void testFindByIdWhenExists() {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(1L);
        entity.setCustomerId(10L);
        entity.setRestaurantId(20L);
        entity.setTotal(new BigDecimal("9.99"));
        entity.setStatus(OrderStatus.CREATED);
        entity.setItems(new ArrayList<>());

        Mockito.when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Order> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(10L, result.get().getCustomerId());
        Mockito.verify(jpaRepository).findById(1L);
    }

    @Test
    void testFindByIdWhenNotExists() {
        Mockito.when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Order> result = adapter.findById(999L);

        assertTrue(result.isEmpty());
        Mockito.verify(jpaRepository).findById(999L);
    }

}
