package com.mtogo.ordering.domain;

import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import com.mtogo.ordering.domain.model.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderDomainTest {

    @Test
    void testCreateOrderCalculatesTotal() {
        Order order = new Order(
                1L,
                10L,
                List.of(
                        new OrderItem(100L, "Burger", new BigDecimal("19.99"), 2),
                        new OrderItem(101L, "Fries", new BigDecimal("5.00"), 1)
                )
        );

        assertEquals(1L, order.getCustomerId());
        assertEquals(10L, order.getRestaurantId());
        assertEquals(new BigDecimal("44.98"), order.getTotal());
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void testConfirmOrder() {
        Order order = new Order(
                1L,
                10L,
                List.of(new OrderItem(100L, "Burger", new BigDecimal("19.99"), 1))
        );

        order.confirm();

        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void testCannotConfirmTwice() {
        Order order = new Order(
                1L,
                10L,
                List.of(new OrderItem(100L, "Burger", new BigDecimal("19.99"), 1))
        );

        order.confirm();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                order::confirm
        );

        assertEquals("Only CREATED orders can be confirmed", exception.getMessage());
    }
}
