package com.mtogo.ordering.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import com.mtogo.ordering.domain.port.in.ConfirmOrderUseCase;
import com.mtogo.ordering.domain.port.in.CreateOrderCommand;
import com.mtogo.ordering.domain.port.in.CreateOrderUseCase;
import com.mtogo.ordering.domain.port.in.GetOrderUseCase;
import com.mtogo.ordering.domain.port.out.OrderEventPublisherPort;
import com.mtogo.ordering.domain.port.out.OrderRepositoryPort;

public class OrderService implements
        CreateOrderUseCase,
        ConfirmOrderUseCase,
        GetOrderUseCase {

    private final OrderRepositoryPort repository;
    private final OrderEventPublisherPort eventPublisher;

    public OrderService(OrderRepositoryPort repository,
                        OrderEventPublisherPort eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Order createOrder(CreateOrderCommand command) {
        List<OrderItem> items = command.items().stream()
                .map(i -> new OrderItem(
                        i.menuItemId(),
                        i.name(),
                        new BigDecimal(i.price()),
                        i.quantity()
                ))
                .collect(Collectors.toList());

        Order order = new Order(command.customerId(), command.restaurantId(), items);
        return repository.save(order);
    }

    @Override
    public Order confirmOrder(Long orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.confirm();
        Order saved = repository.save(order);

        eventPublisher.publishOrderConfirmed(saved);

        return saved;
    }

    @Override
    public Order getOrder(Long orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}