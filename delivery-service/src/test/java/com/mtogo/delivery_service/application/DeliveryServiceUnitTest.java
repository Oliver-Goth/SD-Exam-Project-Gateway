package com.mtogo.delivery_service.application;
import com.mtogo.delivery.application.service.AssignDeliveryService;

import com.mtogo.delivery.domain.model.DeliveryStatus;
import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.model.Location;
import com.mtogo.delivery.domain.model.TrackingInfo;
import com.mtogo.delivery.domain.port.out.DeliveryEventPublisherPort;
import com.mtogo.delivery.domain.port.out.DeliveryTaskRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceUnitTest {

    @Mock
    private AssignDeliveryService assignDeliveryService;
    @Mock
    private DeliveryTaskRepositoryPort deliveryTaskRepositoryPort;
    @Mock
    private DeliveryEventPublisherPort deliveryEventPublisherPort;


    Instant fixedPickupTime = Instant.parse("2025-12-21T10:00:00Z");
    Instant fixedDeliveryTime = Instant.parse("2025-12-21T12:00:00Z");
    Instant fixedLastUpdated = Instant.parse("2025-12-21T09:30:00Z");
    Instant startTime = Instant.parse("2025-12-21T10:00:00Z");
    Instant endTime = Instant.parse("2025-12-21T12:00:00Z");
    List<Location> route = List.of(new Location(55.6761, 12.5683));
    TrackingInfo trackingInfo = new TrackingInfo(route,startTime,endTime);

    @BeforeEach
    void setUp() {
        assignDeliveryService = new AssignDeliveryService(
                deliveryTaskRepositoryPort,
                deliveryEventPublisherPort
        );
    }

    @Test
    void shouldAssignDeliveryAgent() {
        DeliveryTask task = new DeliveryTask(1L,1L,1L,1L,1L, DeliveryStatus.PENDING, fixedPickupTime , fixedDeliveryTime, new Location(55.6761,12.5683), new Location(55.4038,10.4024), trackingInfo, fixedLastUpdated);
        // arrange
        when(deliveryTaskRepositoryPort.findById(anyLong())).thenReturn(Optional.of(task));
        when(deliveryTaskRepositoryPort.save(any(DeliveryTask.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        assignDeliveryService.assignAgent(1L,1L);
        // assert
        verify(deliveryEventPublisherPort).publishDeliveryAssigned(eq(1L), eq(1L), eq(1L));
        assertEquals(DeliveryStatus.ASSIGNED,  task.getDeliveryStatus());
    }

    @Test
    void shouldNotAssignAgentWhenNoAgentsAvailable() {
        DeliveryTask task = new DeliveryTask(1L,1L,1L,1L,1L, DeliveryStatus.PENDING, fixedPickupTime , fixedDeliveryTime, new Location(55.6761,12.5683), new Location(55.4038,10.4024), trackingInfo, fixedLastUpdated);
        // arrange
        when(deliveryTaskRepositoryPort.findById(anyLong())).thenReturn(Optional.of(task));// ingen ledig agenter


        // act
        assignDeliveryService.assignAgent(1L,null);
        // assert
        verify(deliveryEventPublisherPort , never()).publishDeliveryAssigned(anyLong(),anyLong(),anyLong());
        assertEquals(DeliveryStatus.PENDING, task.getDeliveryStatus());
    }
}
