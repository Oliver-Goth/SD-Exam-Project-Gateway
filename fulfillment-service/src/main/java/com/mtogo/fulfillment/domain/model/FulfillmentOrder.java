package com.mtogo.fulfillment.domain.model;

import java.time.Instant;
import java.util.Objects;

public class FulfillmentOrder {
    private Long id;
    private final Long orderId;
    private final Long restaurantId;
    private FulfillmentStatus status;
    private final PreparationDetails preparationDetails;
    private final KitchenNote kitchenNote;
    private Instant createdAt;
    private Instant updatedAt;
    private String assignedStaff;

    public FulfillmentOrder(Long orderId,
                            Long restaurantId,
                            PreparationDetails preparationDetails,
                            KitchenNote kitchenNote) {
        this.orderId = Objects.requireNonNull(orderId, "orderId");
        this.restaurantId = Objects.requireNonNull(restaurantId, "restaurantId");
        this.preparationDetails = Objects.requireNonNull(preparationDetails, "preparationDetails");
        this.kitchenNote = kitchenNote;
        this.status = FulfillmentStatus.ACCEPTED;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public FulfillmentStatus getStatus() {
        return status;
    }

    public PreparationDetails getPreparationDetails() {
        return preparationDetails;
    }

    public KitchenNote getKitchenNote() {
        return kitchenNote;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(String assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setStatus(FulfillmentStatus status) {
        this.status = status;
    }

    public void markInProgress(String staffName) {
        this.status = FulfillmentStatus.IN_PROGRESS;
        this.assignedStaff = staffName;
        this.updatedAt = Instant.now();
    }

    public void markPrepared() {
        this.status = FulfillmentStatus.PREPARED;
        this.updatedAt = Instant.now();
    }
}
