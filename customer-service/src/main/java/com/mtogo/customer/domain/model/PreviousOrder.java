package com.mtogo.customer.domain.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = "CT_CONSTRUCTOR_THROW", justification = "Constructor performs guard clauses to enforce invariants")
public class PreviousOrder {

    private final Long orderId;
    private final String restaurantName;
    private final LocalDateTime orderDate;
    private final BigDecimal totalAmount;
    private final String deliveryStatus;

    public PreviousOrder(Long orderId,
                         String restaurantName,
                         LocalDateTime orderDate,
                         BigDecimal totalAmount,
                         String deliveryStatus) {

        if (orderId == null) throw new IllegalArgumentException("OrderId cannot be null");
        if (restaurantName == null || restaurantName.isEmpty()) throw new IllegalArgumentException("Restaurant name required");
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Total amount must be positive");

        this.orderId = orderId;
        this.restaurantName = restaurantName;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.deliveryStatus = deliveryStatus;
    }

    public Long getOrderId() { return orderId; }
    public String getRestaurantName() { return restaurantName; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getDeliveryStatus() { return deliveryStatus; }
}
