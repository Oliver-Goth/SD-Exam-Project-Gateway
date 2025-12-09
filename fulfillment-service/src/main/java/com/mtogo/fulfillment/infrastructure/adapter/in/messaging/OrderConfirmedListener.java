package com.mtogo.fulfillment.infrastructure.adapter.in.messaging;

import com.mtogo.fulfillment.domain.model.KitchenNote;
import com.mtogo.fulfillment.domain.model.PreparationDetails;
import com.mtogo.fulfillment.domain.port.in.CreateFulfillmentFromOrderUseCase;
import com.mtogo.fulfillment.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderConfirmedListener {

    private final CreateFulfillmentFromOrderUseCase useCase;

    public OrderConfirmedListener(CreateFulfillmentFromOrderUseCase useCase) {
        this.useCase = useCase;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CONFIRMED_QUEUE)
    public void handleOrderConfirmed(@Payload OrderConfirmedMessage message) {
        PreparationDetails details = new PreparationDetails(message.items(), Math.toIntExact(message.estimatedPrepMinutes()));
        KitchenNote note = new KitchenNote(message.specialInstruction());
        useCase.createFromOrder(message.orderId(), message.restaurantId(), details, note);
    }

    public record OrderConfirmedMessage(Long orderId, Long restaurantId, List<String> items, long estimatedPrepMinutes, String specialInstruction) {}
}
