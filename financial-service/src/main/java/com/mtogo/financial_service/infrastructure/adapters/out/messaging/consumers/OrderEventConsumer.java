package com.mtogo.financial_service.infrastructure.adapters.out.messaging.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.mtogo.financial_service.domain.model.Currency;
import com.mtogo.financial_service.domain.port.in.CreatePaymentCommand;
import com.mtogo.financial_service.domain.port.in.CreatePaymentUseCase;
import com.mtogo.financial_service.infrastructure.adapters.out.messaging.events.OrderEvent;


@Component
public class OrderEventConsumer {

    private final CreatePaymentUseCase createPaymentUseCase;
    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);

    public OrderEventConsumer(CreatePaymentUseCase createPaymentUseCase) {
        this.createPaymentUseCase = createPaymentUseCase;
    }

    @RabbitListener(queues = "order.confirmed.queue")
    public void receiveMessage(OrderEvent event) {
        logger.info("Received order event: " + event.getMessage());
        try {
            // Create a payment command from the order event
            CreatePaymentCommand command = new CreatePaymentCommand();
            command.orderId = event.getOrderId();
            command.amount = event.getTotal().doubleValue();
            command.currency = Currency.DKK; // Just DKK for simplicity 
       
            createPaymentUseCase.createPayment(command);
            logger.info("Payment created with success for order with orderID " + event.getOrderId());
        } catch (Exception e) {
            logger.error("Error creating payment for orderId: " + event.getOrderId(), e);
            throw new RuntimeException("Failed to create payment", e);
        }
    }
}
