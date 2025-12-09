package com.mtogo.fulfillment.infrastructure.adapter.out.persistence;

import com.mtogo.fulfillment.domain.model.FulfillmentOrder;
import com.mtogo.fulfillment.domain.model.FulfillmentStatus;
import com.mtogo.fulfillment.domain.model.KitchenNote;
import com.mtogo.fulfillment.domain.model.PreparationDetails;
import org.springframework.stereotype.Component;

@Component
public class FulfillmentMapper {

    public FulfillmentJpaEntity toEntity(FulfillmentOrder order) {
        FulfillmentJpaEntity entity = new FulfillmentJpaEntity();
        entity.setId(order.getId());
        entity.setOrderId(order.getOrderId());
        entity.setRestaurantId(order.getRestaurantId());
        entity.setStatus(order.getStatus());
        entity.setItemList(order.getPreparationDetails().itemList());
        entity.setEstimatedMinutes(order.getPreparationDetails().estimatedMinutes());
        entity.setSpecialInstruction(order.getKitchenNote() != null ? order.getKitchenNote().text() : null);
        entity.setAssignedStaff(order.getAssignedStaff());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());
        return entity;
    }

    public FulfillmentOrder toDomain(FulfillmentJpaEntity entity) {
        PreparationDetails details = new PreparationDetails(
                entity.getItemList(),
                entity.getEstimatedMinutes()
        );
        KitchenNote note = new KitchenNote(entity.getSpecialInstruction());
        FulfillmentOrder order = new FulfillmentOrder(
                entity.getOrderId(),
                entity.getRestaurantId(),
                details,
                note
        );
        order.setId(entity.getId());
        order.setStatus(entity.getStatus());
        order.setAssignedStaff(entity.getAssignedStaff());
        order.setCreatedAt(entity.getCreatedAt());
        order.setUpdatedAt(entity.getUpdatedAt());
        return order;
    }
}
