package com.mtogo.delivery.infrastructure.adapter.out.persistence;

import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.model.Location;
import com.mtogo.delivery.domain.model.TrackingInfo;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeliveryTaskMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DeliveryTask toDomain(DeliveryTaskJpaEntity e) {
        Location pickup = e.getPickupLatitude() != null && e.getPickupLongitude() != null
                ? new Location(e.getPickupLatitude(), e.getPickupLongitude()) : null;
        Location delivery = e.getDeliveryLatitude() != null && e.getDeliveryLongitude() != null
                ? new Location(e.getDeliveryLatitude(), e.getDeliveryLongitude()) : null;

        TrackingInfo tracking = null;
        try {
            if (e.getTrackingJson() != null) {
                TrackingDto dto = objectMapper.readValue(e.getTrackingJson(), TrackingDto.class);
                List<Location> route = dto.route.stream().map(r -> new Location(r.latitude, r.longitude)).collect(Collectors.toList());
                tracking = new TrackingInfo(route, Instant.parse(dto.startTime), Instant.parse(dto.endTime));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse tracking json", ex);
        }

        return new DeliveryTask(
                e.getDeliveryId(),
                e.getOrderId(),
                e.getRestaurantId(),
                e.getCustomerId(),
                e.getAgentId(),
                e.getDeliveryStatus(),
                e.getPickupTime(),
                e.getDeliveryTime(),
                pickup,
                delivery,
                tracking,
                e.getLastUpdatedAt()
        );
    }

    public DeliveryTaskJpaEntity toEntity(DeliveryTask domain) {
        DeliveryTaskJpaEntity e = new DeliveryTaskJpaEntity();
        e.setDeliveryId(domain.getDeliveryId());
        e.setOrderId(domain.getOrderId());
        e.setRestaurantId(domain.getRestaurantId());
        e.setCustomerId(domain.getCustomerId());
        e.setAgentId(domain.getAgentId());
        e.setDeliveryStatus(domain.getDeliveryStatus());
        e.setPickupTime(domain.getPickupTime());
        e.setDeliveryTime(domain.getDeliveryTime());
        if (domain.getPickupLocation() != null) {
            e.setPickupLatitude(domain.getPickupLocation().latitude());
            e.setPickupLongitude(domain.getPickupLocation().longitude());
        }
        if (domain.getDeliveryLocation() != null) {
            e.setDeliveryLatitude(domain.getDeliveryLocation().latitude());
            e.setDeliveryLongitude(domain.getDeliveryLocation().longitude());
        }

        if (domain.getTrackingInfo() != null) {
            try {
                TrackingDto dto = new TrackingDto();
                dto.startTime = domain.getTrackingInfo().startTime().toString();
                dto.endTime = domain.getTrackingInfo().endTime().toString();
                dto.route = domain.getTrackingInfo().route().stream().map(loc -> new LocDto(loc.latitude(), loc.longitude())).collect(Collectors.toList());
                e.setTrackingJson(objectMapper.writeValueAsString(dto));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to serialize tracking info", ex);
            }
        }
        e.setLastUpdatedAt(domain.getLastUpdatedAt());
        return e;
    }

    static class TrackingDto {
        public java.util.List<LocDto> route;
        public String startTime;
        public String endTime;
    }
    static class LocDto {
        public double latitude;
        public double longitude;
        public LocDto() {}
        public LocDto(double lat, double lon) { this.latitude = lat; this.longitude = lon; }
    }
}
