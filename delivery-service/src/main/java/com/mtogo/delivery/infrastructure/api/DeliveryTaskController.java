package com.mtogo.delivery.infrastructure.api;

import com.example.AuditLogger;
import com.example.RequestLogger;
import com.example.TimeIt;
import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.model.Location;
import com.mtogo.delivery.domain.port.in.CreateDeliveryUseCase;
import com.mtogo.delivery.domain.port.in.UpdateDeliveryStatusUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/deliveries")
public class DeliveryTaskController {

    private final CreateDeliveryUseCase createUseCase;
    private final UpdateDeliveryStatusUseCase updateUseCase;
    private static final Logger log = LoggerFactory.getLogger(DeliveryTaskController.class);

    public DeliveryTaskController(CreateDeliveryUseCase createUseCase, UpdateDeliveryStatusUseCase updateUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
    }

    @PostMapping
    public DeliveryTaskResponse create(@RequestBody DeliveryTaskRequest request) {
        String userId = String.valueOf(request.customerId());

        DeliveryTask task =
                RequestLogger.log(
                        userId,
                        "POST",
                        "/deliveries",
                        201,
                        () -> {

                            DeliveryTask created =
                                    TimeIt.info(log, "Create delivery task", () ->
                                            createUseCase.createForOrder(
                                                    request.orderId(),
                                                    request.restaurantId(),
                                                    request.customerId()
                                            )
                                    );

                            AuditLogger.log(
                                    "DELIVERY_CREATE",
                                    userId,
                                    "delivery:" + created.getDeliveryId(),
                                    "203.0.113.7"
                            );

                            return created;
                        }
                );

        return toResponse(task);
    }

    @PostMapping("/{id}/assign")
    public DeliveryTaskResponse assignAgent(@PathVariable("id") Long deliveryId, @RequestBody AssignAgentRequest request){
        String agent_Id = String.valueOf(request.agentId());

        DeliveryTask task =
                RequestLogger.log(
                        agent_Id,
                        "POST",
                        "/deliveries/{id}/assign",
                        201,
                        () -> {

                            DeliveryTask created =
                                    TimeIt.info(log, "Create delivery task", () ->
                                            updateUseCase.assignAgent(
                                                    deliveryId,
                                                    request.agentId()
                                            )
                                    );

                            AuditLogger.log(
                                    "DELIVERY_CREATE",
                                    agent_Id,
                                    "delivery:" + created.getDeliveryId(),
                                    "203.0.113.7"
                            );

                            return created;
                        }
                );

        return toResponse(task);
    }


    @PostMapping("/{id}/pickup")
    public DeliveryTaskResponse pickUp(@PathVariable("id") Long deliveryId, @RequestBody LocationDto loc){

        DeliveryTask task =
                RequestLogger.log(
                        "agent",
                        "POST",
                        "/deliveries/{id}/pickup",
                        200,
                        () -> {

                            DeliveryTask pickedUp =
                                    TimeIt.info(log, "Mark delivery picked up", () ->
                                            updateUseCase.markPickedUp(
                                                    deliveryId,
                                                    Instant.now(),
                                                    new Location(loc.latitude(), loc.longitude())

                                            )
                                    );

                            AuditLogger.log(
                                "DELIVERY_PICkUP",
                                    String.valueOf(pickedUp.getDeliveryId()),
                                    "delivery: " + pickedUp.getDeliveryId(),
                                    "203.0.113.7"

                            );

                            return pickedUp;
                        }
                );

        return toResponse(task);
    }

    @PostMapping("/{id}/deliver")
    public DeliveryTaskResponse deliver(@PathVariable("id") Long deliveryId, @RequestBody LocationDto loc){

        DeliveryTask task =
                RequestLogger.log(
                        "agent",
                        "POST",
                        "/deliveries/{id}/deliver",
                        200,
                        () -> {

                            DeliveryTask delivered =
                                    TimeIt.info(log, "Delivered", () ->
                                            updateUseCase.markDelivered(
                                                    deliveryId,
                                                    Instant.now(),
                                                    new Location(loc.latitude(), loc.longitude())

                                            )
                                    );

                            AuditLogger.log(
                                    "DELIVERY_DELIVERED",
                                    String.valueOf(delivered.getAgentId()),
                                    "delivery: " + delivered.getDeliveryId(),
                                    "203.0.113.7"

                            );

                            return delivered;
                        }
                );

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
