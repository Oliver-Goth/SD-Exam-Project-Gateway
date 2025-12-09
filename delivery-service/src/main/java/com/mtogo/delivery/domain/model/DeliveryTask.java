package com.mtogo.delivery.domain.model;

import java.time.Instant;
import java.util.Objects;

public class DeliveryTask {

    private final Long deliveryId;
    private final Long orderId;
    private final Long restaurantId;
    private final Long customerId;

    // mutable attributes
    private Long agentId;
    private DeliveryStatus deliveryStatus;
    private Instant pickupTime;
    private Instant deliveryTime;
    private Location pickupLocation;
    private Location deliveryLocation;
    private TrackingInfo trackingInfo;
    private Instant lastUpdatedAt;

    // constructor for reconstructing from persistence
    public DeliveryTask(Long deliveryId,
                        Long orderId,
                        Long restaurantId,
                        Long customerId,
                        Long agentId,
                        DeliveryStatus deliveryStatus,
                        Instant pickupTime,
                        Instant deliveryTime,
                        Location pickupLocation,
                        Location deliveryLocation,
                        TrackingInfo trackingInfo,
                        Instant lastUpdatedAt) {
        this.deliveryId = Objects.requireNonNull(deliveryId);
        this.orderId = Objects.requireNonNull(orderId);
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.agentId = agentId;
        this.deliveryStatus = deliveryStatus == null ? DeliveryStatus.PENDING : deliveryStatus;
        this.pickupTime = pickupTime;
        this.deliveryTime = deliveryTime;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.trackingInfo = trackingInfo;
        this.lastUpdatedAt = lastUpdatedAt == null ? Instant.now() : lastUpdatedAt;
    }

    public static DeliveryTask createForOrder(Long deliveryId, Long orderId, Long restaurantId, Long customerId) {
        return new DeliveryTask(deliveryId, orderId, restaurantId, customerId, null, DeliveryStatus.PENDING, null, null, null, null, null, Instant.now());
    }

    public void assignAgent(Long agentId) {
        if (this.deliveryStatus != DeliveryStatus.PENDING) {
            throw new IllegalStateException("Can only assign agent when delivery is pending");
        }
        this.agentId = Objects.requireNonNull(agentId);
        this.deliveryStatus = DeliveryStatus.ASSIGNED;
        this.lastUpdatedAt = Instant.now();
    }

    public void markPickedUp(Instant pickupTime, Location pickupLocation) {
        if (this.deliveryStatus != DeliveryStatus.ASSIGNED) {
            throw new IllegalStateException("Delivery must be ASSIGNED before picking up");
        }
        this.pickupTime = Objects.requireNonNull(pickupTime);
        this.pickupLocation = Objects.requireNonNull(pickupLocation);
        this.deliveryStatus = DeliveryStatus.PICKED_UP;
        this.lastUpdatedAt = Instant.now();
    }

    public void markDelivered(Instant deliveryTime, Location deliveryLocation) {
        if (this.deliveryStatus != DeliveryStatus.PICKED_UP && this.deliveryStatus != DeliveryStatus.ASSIGNED) {
            throw new IllegalStateException("Delivery must be picked up (or at least assigned) before being delivered");
        }
        this.deliveryTime = Objects.requireNonNull(deliveryTime);
        this.deliveryLocation = Objects.requireNonNull(deliveryLocation);
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.lastUpdatedAt = Instant.now();
    }

    public void setTrackingInfo(TrackingInfo trackingInfo) {
        if (this.trackingInfo != null) {
            throw new IllegalStateException("TrackingInfo is read-only once set");
        }
        this.trackingInfo = Objects.requireNonNull(trackingInfo);
        this.lastUpdatedAt = Instant.now();
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public Instant getPickupTime() {
        return pickupTime;
    }

    public Instant getDeliveryTime() {
        return deliveryTime;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDeliveryLocation() {
        return deliveryLocation;
    }

    public TrackingInfo getTrackingInfo() {
        return trackingInfo;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }
}
