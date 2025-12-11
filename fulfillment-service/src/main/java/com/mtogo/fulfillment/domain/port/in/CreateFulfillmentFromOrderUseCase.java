package com.mtogo.fulfillment.domain.port.in;

import com.mtogo.fulfillment.domain.model.FulfillmentOrder;
import com.mtogo.fulfillment.domain.model.KitchenNote;
import com.mtogo.fulfillment.domain.model.PreparationDetails;

public interface CreateFulfillmentFromOrderUseCase {
    FulfillmentOrder createFromOrder(Long orderId,
                                     Long restaurantId,
                                     Long customerId,
                                     PreparationDetails preparationDetails,
                                     KitchenNote kitchenNote);
}
