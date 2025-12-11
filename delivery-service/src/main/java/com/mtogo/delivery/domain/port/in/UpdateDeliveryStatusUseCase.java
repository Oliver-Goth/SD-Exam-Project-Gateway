package com.mtogo.delivery.domain.port.in;

import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.model.Location;
import java.time.Instant;

public interface UpdateDeliveryStatusUseCase {
    DeliveryTask assignAgent(Long deliveryId, Long agentId);
    DeliveryTask markPickedUp(Long deliveryId, Instant pickupTime, Location pickupLocation);
    DeliveryTask markDelivered(Long deliveryId, Instant deliveryTime, Location deliveryLocation);
    DeliveryTask setTrackingInfo(Long deliveryId, com.mtogo.delivery.domain.model.TrackingInfo trackingInfo);
}
