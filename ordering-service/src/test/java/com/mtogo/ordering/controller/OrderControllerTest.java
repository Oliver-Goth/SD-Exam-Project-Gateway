package com.mtogo.ordering.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import com.mtogo.ordering.domain.model.OrderStatus;
import com.mtogo.ordering.domain.port.in.ConfirmOrderUseCase;
import com.mtogo.ordering.domain.port.in.CreateOrderCommand;
import com.mtogo.ordering.domain.port.in.CreateOrderUseCase;
import com.mtogo.ordering.domain.port.in.GetOrderUseCase;
import com.mtogo.ordering.infrastructure.adapter.in.web.OrderController;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @MockBean
    private ConfirmOrderUseCase confirmOrderUseCase;

    @MockBean
    private GetOrderUseCase getOrderUseCase;

    @Test
    void testCreateOrder() throws Exception {
        Order order = sampleOrder(1L);

        Mockito.when(createOrderUseCase.createOrder(ArgumentMatchers.any(CreateOrderCommand.class)))
                .thenReturn(order);

        mvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": 1,
                                  "restaurantId": 2,
                                  "items": [
                                    {
                                      "menuItemId": 100,
                                      "name": "Burger",
                                      "price": "9.99",
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.restaurantId").value(2))
                .andExpect(jsonPath("$.items[0].name").value("Burger"));
    }

    @Test
    void testGetOrder() throws Exception {
        Order order = sampleOrder(5L);
        Mockito.when(getOrderUseCase.getOrder(5L)).thenReturn(order);

        mvc.perform(get("/orders/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    private Order sampleOrder(Long id) {
        Order order = new Order(
                1L,
                2L,
                List.of(new OrderItem(100L, "Burger", new BigDecimal("9.99"), 1))
        );
        order.setId(id);
        return order;
    }
}
