package com.mtogo.ordering.application;

import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import com.mtogo.ordering.domain.port.in.CreateOrderCommand;
import com.mtogo.ordering.domain.port.out.OrderEventPublisherPort;
import com.mtogo.ordering.domain.port.out.OrderRepositoryPort;
import com.mtogo.ordering.domain.service.OrderService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

class OrderServiceUnitTest {

    private OrderRepositoryPort repository;
    private OrderEventPublisherPort eventPublisher;
    private OrderService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(OrderRepositoryPort.class);
        eventPublisher = Mockito.mock(OrderEventPublisherPort.class);
        service = new OrderService(repository, eventPublisher);
    }

    @Test
    void testCreateOrder() {
        CreateOrderCommand command = new CreateOrderCommand(
                10L,
                20L,
                List.of(new CreateOrderCommand.CreateOrderItem(100L, "Burger", "9.99", 2))
        );

        Mockito.when(repository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order o = invocation.getArgument(0);
                    o.setId(1L);
                    return o;
                });

        Order created = service.createOrder(command);

        assertNotNull(created.getId());
        assertEquals(10L, created.getCustomerId());
        assertEquals(20L, created.getRestaurantId());

        Mockito.verify(repository).save(any(Order.class));
        Mockito.verify(eventPublisher, Mockito.never()).publishOrderConfirmed(any());
    }

    @Test
    void testConfirmOrderPublishesEvent() {
        Order order = new Order(
                10L,
                20L,
                List.of(new OrderItem(100L, "Burger", new BigDecimal("9.99"), 1))
        );
        order.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order confirmed = service.confirmOrder(1L);

        assertEquals(1L, confirmed.getId());
        assertEquals("CONFIRMED", confirmed.getStatus().name());
        Mockito.verify(eventPublisher).publishOrderConfirmed(order);
    }
}
