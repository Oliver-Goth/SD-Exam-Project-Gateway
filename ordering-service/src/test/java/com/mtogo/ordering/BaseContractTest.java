package com.mtogo.ordering;

import com.mtogo.ordering.domain.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.sql.DataSource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class BaseContractTest {

    @Autowired
    protected OrderService orderService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    

    protected void confirmOrder() {
        orderService.confirmOrder(8L);
    }
}
