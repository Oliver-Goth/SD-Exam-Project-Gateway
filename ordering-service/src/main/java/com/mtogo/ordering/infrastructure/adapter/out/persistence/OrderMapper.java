package com.mtogo.ordering.infrastructure.adapter.out.persistence;

import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public OrderJpaEntity toEntity(Order domain) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(domain.getId());
        entity.setCustomerId(domain.getCustomerId());
        entity.setRestaurantId(domain.getRestaurantId());
        entity.setTotal(domain.getTotal());
        entity.setStatus(domain.getStatus());

        var items = domain.getItems().stream().map(item -> {
            OrderItemJpaEntity i = new OrderItemJpaEntity();
            i.setMenuItemId(item.getMenuItemId());
            i.setName(item.getName());
            i.setPrice(item.getPrice());
            i.setQuantity(item.getQuantity());
            i.setOrder(entity);
            return i;
        }).collect(Collectors.toList());

        entity.setItems(items);

        return entity;
    }

    public Order toDomain(OrderJpaEntity entity) {
        var items = entity.getItems().stream()
                .map(i -> new OrderItem(
                        i.getMenuItemId(),
                        i.getName(),
                        i.getPrice(),
                        i.getQuantity()
                ))
                .collect(Collectors.toList());

        Order order = new Order(entity.getCustomerId(), entity.getRestaurantId(), items);
        order.setId(entity.getId());
        order.setStatus(entity.getStatus());
        return order;
    }
}
