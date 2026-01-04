package com.mtogo.ordering.adapter;

import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import com.mtogo.ordering.domain.model.OrderStatus;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderItemJpaEntity;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderJpaEntity;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private OrderMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new OrderMapper();
    }

    @Test
    void testToEntityMapsAllFields() {
        Order domain = new Order(
                10L,
                20L,
                List.of(new OrderItem(100L, "Burger", new BigDecimal("9.99"), 2)));
        domain.setId(1L);

        OrderJpaEntity entity = mapper.toEntity(domain);

        assertEquals(1L, entity.getId());
        assertEquals(10L, entity.getCustomerId());
        assertEquals(20L, entity.getRestaurantId());
        assertEquals(new BigDecimal("19.98"), entity.getTotal());
        assertEquals(OrderStatus.CREATED, entity.getStatus());
        assertEquals(1, entity.getItems().size());
    }

    @Test
    void testToEntityMapsItems() {
        Order domain = new Order(
                10L,
                20L,
                List.of(
                        new OrderItem(100L, "Burger", new BigDecimal("9.99"), 2),
                        new OrderItem(101L, "Fries", new BigDecimal("5.00"), 1)));
        domain.setId(1L);

        OrderJpaEntity entity = mapper.toEntity(domain);

        assertEquals(2, entity.getItems().size());
        assertEquals(100L, entity.getItems().get(0).getMenuItemId());
        assertEquals("Burger", entity.getItems().get(0).getName());
        assertEquals(new BigDecimal("9.99"), entity.getItems().get(0).getPrice());
        assertEquals(2, entity.getItems().get(0).getQuantity());
        assertEquals(entity, entity.getItems().get(0).getOrder());
    }

    @Test
    void testToDomainMapsAllFields() {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(1L);
        entity.setCustomerId(10L);
        entity.setRestaurantId(20L);
        entity.setTotal(new BigDecimal("19.99"));
        entity.setStatus(OrderStatus.CONFIRMED);
        entity.setItems(new ArrayList<>());

        Order domain = mapper.toDomain(entity);

        assertEquals(1L, domain.getId());
        assertEquals(10L, domain.getCustomerId());
        assertEquals(20L, domain.getRestaurantId());
        assertEquals(OrderStatus.CONFIRMED, domain.getStatus());
        assertNotNull(domain.getItems());
    }

    @Test
    void testToDomainMapsItems() {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(1L);
        entity.setCustomerId(10L);
        entity.setRestaurantId(20L);
        entity.setTotal(new BigDecimal("19.99"));
        entity.setStatus(OrderStatus.CREATED);

        OrderItemJpaEntity item1 = new OrderItemJpaEntity();
        item1.setMenuItemId(100L);
        item1.setName("Burger");
        item1.setPrice(new BigDecimal("9.99"));
        item1.setQuantity(2);

        OrderItemJpaEntity item2 = new OrderItemJpaEntity();
        item2.setMenuItemId(101L);
        item2.setName("Fries");
        item2.setPrice(new BigDecimal("5.00"));
        item2.setQuantity(1);

        List<OrderItemJpaEntity> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        entity.setItems(items);

        Order domain = mapper.toDomain(entity);

        assertEquals(2, domain.getItems().size());
        assertEquals(100L, domain.getItems().get(0).getMenuItemId());
        assertEquals("Burger", domain.getItems().get(0).getName());
        assertEquals(new BigDecimal("9.99"), domain.getItems().get(0).getPrice());
        assertEquals(2, domain.getItems().get(0).getQuantity());
    }

}
