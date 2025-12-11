package com.mtogo.delivery.infrastructure.api;

import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.model.Location;
import com.mtogo.delivery.domain.port.in.CreateDeliveryUseCase;
import com.mtogo.delivery.domain.port.in.UpdateDeliveryStatusUseCase;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/deliveries")
public class DeliveryTaskController {

    private final CreateDeliveryUseCase createUseCase;
    private final UpdateDeliveryStatusUseCase updateUseCase;

    public DeliveryTaskController(CreateDeliveryUseCase createUseCase, UpdateDeliveryStatusUseCase updateUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
    }

    @PostMapping
    public DeliveryTaskResponse create(@RequestBody DeliveryTaskRequest request) {
        DeliveryTask task = createUseCase.createForOrder(request.orderId(), request.restaurantId(), request.customerId());
        return toResponse(task);
    }

    @PostMapping("/{id}/assign")
    public DeliveryTaskResponse assignAgent(@PathVariable("id") Long deliveryId, @RequestBody AssignAgentRequest req) {
        DeliveryTask task = updateUseCase.assignAgent(deliveryId, req.agentId());
        return toResponse(task);
    }

    @PostMapping("/{id}/pickup")
    public DeliveryTaskResponse pickup(@PathVariable("id") Long deliveryId, @RequestBody LocationDto loc) {
        DeliveryTask task = updateUseCase.markPickedUp(deliveryId, Instant.now(), new Location(loc.latitude(), loc.longitude()));
        return toResponse(task);
    }

    @PostMapping("/{id}/deliver")
    public DeliveryTaskResponse deliver(@PathVariable("id") Long deliveryId, @RequestBody LocationDto loc) {
        DeliveryTask task = updateUseCase.markDelivered(deliveryId, Instant.now(), new Location(loc.latitude(), loc.longitude()));
        return toResponse(task);
    }

    private DeliveryTaskResponse toResponse(DeliveryTask t) {
        return new DeliveryTaskResponse(
                t.getDeliveryId(),
                t.getOrderId(),
                t.getRestaurantId(),
                t.getCustomerId(),
                t.getAgentId(),
                t.getDeliveryStatus().name(),
                t.getPickupTime(),
                t.getDeliveryTime()
        );
    }
}
