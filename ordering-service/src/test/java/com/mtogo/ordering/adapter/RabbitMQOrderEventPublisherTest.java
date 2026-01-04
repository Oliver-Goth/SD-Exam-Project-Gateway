package com.mtogo.ordering.adapter;

import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import com.mtogo.ordering.infrastructure.adapter.out.messaging.RabbitMQOrderEventPublisher;
import com.mtogo.ordering.infrastructure.config.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class RabbitMQOrderEventPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private RabbitMQOrderEventPublisher publisher;

    @BeforeEach
    void setup() {
        rabbitTemplate = Mockito.mock(RabbitTemplate.class);
        publisher = new RabbitMQOrderEventPublisher(rabbitTemplate);
    }

    @Test
    void testPublishOrderConfirmedSendsMessage() {
        Order order = new Order(
                10L,
                20L,
                List.of(new OrderItem(100L, "Burger", new BigDecimal("9.99"), 2)));
        order.setId(1L);

        publisher.publishOrderConfirmed(order);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.ORDER_EXCHANGE),
                eq(RabbitMQConfig.ORDER_CONFIRMED_ROUTING_KEY),
                captor.capture());

        Map<String, Object> payload = captor.getValue();
        assertEquals(1L, payload.get("orderId"));
        assertEquals(10L, payload.get("customerId"));
        assertEquals(20L, payload.get("restaurantId"));
        assertEquals("19.98", payload.get("total"));
    }

    @Test
    void testPublishOrderConfirmedIncludesItems() {
        Order order = new Order(
                10L,
                20L,
                List.of(
                        new OrderItem(100L, "Burger", new BigDecimal("9.99"), 2),
                        new OrderItem(101L, "Fries", new BigDecimal("5.00"), 1)));
        order.setId(1L);

        publisher.publishOrderConfirmed(order);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(rabbitTemplate).convertAndSend(
                any(String.class),
                any(String.class),
                captor.capture());

        Map<String, Object> payload = captor.getValue();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");

        assertEquals(2, items.size());
        assertEquals(100L, items.get(0).get("menuItemId"));
        assertEquals("Burger", items.get(0).get("name"));
        assertEquals("9.99", items.get(0).get("price"));
        assertEquals(2, items.get(0).get("quantity"));
    }

    @Test
    void testPublishOrderConfirmedWithSingleItem() {
        Order order = new Order(
                5L,
                15L,
                List.of(new OrderItem(200L, "Pizza", new BigDecimal("15.99"), 1)));
        order.setId(10L);

        publisher.publishOrderConfirmed(order);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.ORDER_EXCHANGE),
                eq(RabbitMQConfig.ORDER_CONFIRMED_ROUTING_KEY),
                captor.capture());

        Map<String, Object> payload = captor.getValue();
        assertEquals(10L, payload.get("orderId"));
        assertEquals(5L, payload.get("customerId"));
        assertEquals(15L, payload.get("restaurantId"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");
        assertEquals(1, items.size());
        assertEquals(200L, items.get(0).get("menuItemId"));
        assertEquals("Pizza", items.get(0).get("name"));
    }

}
