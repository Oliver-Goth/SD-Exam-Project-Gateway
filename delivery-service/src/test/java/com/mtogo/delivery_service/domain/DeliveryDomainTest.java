package com.mtogo.delivery_service.domain;


import com.mtogo.delivery.domain.model.DeliveryStatus;
import com.mtogo.delivery.domain.model.DeliveryTask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryDomainTest {

    @Test
    void assignAgent_setsAgentAndStatus() {
        DeliveryTask task = DeliveryTask.createForOrder(
                1L,  // deliveryId
                10L, // orderId
                20L, // restaurantId
                30L  // customerId
        );

        task.assignAgent(99L);

        assertEquals(99L, task.getAgentId());
        assertEquals(DeliveryStatus.ASSIGNED, task.getDeliveryStatus());
    }
}



