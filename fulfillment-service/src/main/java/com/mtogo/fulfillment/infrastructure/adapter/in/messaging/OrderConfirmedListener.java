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
        List<String> items = message.items() != null ? message.items() : List.of();
        long prepMinutes = message.estimatedPrepMinutes() != null ? message.estimatedPrepMinutes() : 15L;
        PreparationDetails details = new PreparationDetails(items, Math.toIntExact(prepMinutes));
        KitchenNote note = new KitchenNote(message.specialInstruction());
        useCase.createFromOrder(message.orderId(), message.restaurantId(), message.customerId(), details, note);
    }

    public record OrderConfirmedMessage(Long orderId,
                                        Long restaurantId,
                                        Long customerId,
                                        List<String> items,
                                        Long estimatedPrepMinutes,
                                        String specialInstruction) {}
}
