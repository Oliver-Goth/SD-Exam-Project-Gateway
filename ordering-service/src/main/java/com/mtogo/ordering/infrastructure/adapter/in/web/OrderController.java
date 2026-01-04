package com.mtogo.ordering.infrastructure.adapter.in.web;

import com.example.AuditLogger;
import com.example.RequestLogger;
import com.example.TimeIt;
import com.mtogo.ordering.application.dto.ConfirmOrderRequest;
import com.mtogo.ordering.application.dto.CreateOrderRequest;
import com.mtogo.ordering.application.dto.OrderItemDto;
import com.mtogo.ordering.application.dto.OrderResponse;
import com.mtogo.ordering.domain.model.Order;
import com.mtogo.ordering.domain.model.OrderItem;
import com.mtogo.ordering.domain.port.in.ConfirmOrderUseCase;
import com.mtogo.ordering.domain.port.in.CreateOrderCommand;
import com.mtogo.ordering.domain.port.in.CreateOrderUseCase;
import com.mtogo.ordering.domain.port.in.GetOrderUseCase;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           ConfirmOrderUseCase confirmOrderUseCase,
                           GetOrderUseCase getOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    @PostMapping
    public OrderResponse create(@RequestBody CreateOrderRequest req) {
        var items = req.items().stream()
                .map(i -> new CreateOrderCommand.CreateOrderItem(
                        i.menuItemId(),
                        i.name(),
                        i.price(),
                        i.quantity()
                ))
                .toList();

        CreateOrderCommand cmd = new CreateOrderCommand(
                req.customerId(),
                req.restaurantId(),
                items
        );

        Order order = createOrderUseCase.createOrder(cmd);
        return toResponse(order);
    }

   @PostMapping("/{id}/confirm")
   public OrderResponse confirm(@PathVariable Long id) {
       String actorId = String.valueOf(id);
       Order order =
               RequestLogger.log(
                       actorId,
                       "POST",
                       "/orders/{id}/confirm",
                       200,
                       () -> {

                           Order confirmed =
                                   TimeIt.info(log, "Order Confirmed", () ->
                                           confirmOrderUseCase.confirmOrder(id)
                                   );

                           AuditLogger.log(
                                   "CONFIRM_ORDER",
                                   actorId,
                                   "order confirmation:" + confirmed.getId(),
                                   "203.0.113.7"
                           );

                           return confirmed;
                       }
               );

       return toResponse(order);
   }

   @GetMapping("/{id}")
   public OrderResponse getById(@PathVariable Long id) {
       String actorId = String.valueOf(id);

       Order order =
               RequestLogger.log(
                       actorId,
                       "GET",
                       "/orders/{id}",
                       200,
                       () -> {

                           Order fetched =
                                   TimeIt.info(log, "Get order by id", () ->
                                           getOrderUseCase.getOrder(id)
                                   );

                           AuditLogger.log(
                                   "ORDER-READ",
                                   actorId,
                                   "order confirmation:" + fetched.getId(),
                                   "203.0.113.7"
                           );

                           return fetched;
                       }
               );

       return toResponse(order);
   }

    private OrderResponse toResponse(Order o) {
        List<OrderItemDto> items = o.getItems().stream()
                .map(this::toItemDto)
                .toList();

        return new OrderResponse(
                o.getId(),
                o.getCustomerId(),
                o.getRestaurantId(),
                items,
                o.getTotal(),
                o.getStatus()
        );
    }

    private OrderItemDto toItemDto(OrderItem i) {
        return new OrderItemDto(
                i.getMenuItemId(),
                i.getName(),
                i.getPrice(),
                i.getQuantity()
        );
    }
}
