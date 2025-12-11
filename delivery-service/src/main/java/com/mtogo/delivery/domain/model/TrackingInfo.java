package com.mtogo.delivery.domain.model;

import java.time.Instant;
import java.util.List;

public record TrackingInfo(
        List<Location> route,
        Instant startTime,
        Instant endTime
) {
    public TrackingInfo {
        if (route == null || route.isEmpty()) {
            throw new IllegalArgumentException("Route must contain at least one location");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime and endTime are required");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("endTime cannot be before startTime");
        }
    }
}
