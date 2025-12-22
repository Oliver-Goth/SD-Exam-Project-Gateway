package com.mtogo.delivery.controller;

import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.model.Location;
import com.mtogo.delivery.domain.port.in.CreateDeliveryUseCase;
import com.mtogo.delivery.domain.port.in.UpdateDeliveryStatusUseCase;
import com.mtogo.delivery.infrastructure.api.DeliveryTaskController;
import com.mtogo.delivery.infrastructure.api.LocationDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryTaskController.class)
@WithMockUser
public class DeliveryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateDeliveryUseCase createDeliveryUseCase;

    @MockBean
    private UpdateDeliveryStatusUseCase updateDeliveryStatusUseCase;

    @Test
    void testCreate() throws Exception{
        DeliveryTask deliveryTask = sampleDelivery(1L);

        Mockito.when(createDeliveryUseCase.createForOrder(
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyLong()))
                .thenReturn(deliveryTask);

        mvc.perform(post("/deliveries")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "orderId": 1,
                          "restaurantId": 1,
                          "customerId": 1
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.restaurantId").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testAssignAgent() throws Exception{
        DeliveryTask deliveryTask = sampleDelivery(1L);
        deliveryTask.assignAgent(42L);

        Mockito.when(updateDeliveryStatusUseCase.assignAgent(
                ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()
        )).thenReturn(deliveryTask);

        mvc.perform(post("/deliveries/{id}/assign", 1L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "agentId": 42
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryId").value(1))
                .andExpect(jsonPath("$.agentId").value(42))
                .andExpect(jsonPath("$.status").value("ASSIGNED"));
    }

    @Test
    void testPickUp() throws Exception{
        Long deliveryId = 1L;
        LocationDto pickUpDto = new LocationDto(55.6761, 12.5683);
        Location pickUpLocation = new Location(pickUpDto.latitude(), pickUpDto.longitude());


        DeliveryTask deliveryTask = sampleDelivery(deliveryId);
        deliveryTask.assignAgent(42L);
        Instant now = Instant.now();

        deliveryTask.markPickedUp(now, pickUpLocation);
        Mockito.when(updateDeliveryStatusUseCase.markPickedUp(
                Mockito.eq(deliveryId),
                Mockito.any(Instant.class),
                Mockito.eq(pickUpLocation)
                )).thenReturn(deliveryTask);

        mvc.perform(post("/deliveries/" + deliveryId + "/pickup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "latitude": 55.6761,
                            "longitude": 12.5683
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(deliveryTask.getOrderId()))
                .andExpect(jsonPath("$.agentId").value(deliveryTask.getAgentId()))
                .andExpect(jsonPath("$.status").value("PICKED_UP"));
    }

    @Test
    void testDeliver() throws Exception{
        Long deliveryId = 1L;
        Instant now = Instant.now();

        DeliveryTask deliveryTask = sampleDelivery(deliveryId);
        deliveryTask.assignAgent(1L);

        Location pickupLocation = new Location(55.6761, 12.5683);
        deliveryTask.markPickedUp(now, pickupLocation);
        Location deliveryLocation = new Location(55.6800, 12.5700);
        deliveryTask.markDelivered(now,deliveryLocation);


        Mockito.when(updateDeliveryStatusUseCase.markDelivered(
                Mockito.eq(deliveryId),
                Mockito.any(Instant.class),
                Mockito.any(Location.class)
        )).thenReturn(deliveryTask);

        mvc.perform(post("/deliveries/" + deliveryId + "/deliver")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "latitude": 55.6800,
                        "longitude": 12.5700
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));


    }

    private DeliveryTask sampleDelivery(Long id){
        return DeliveryTask.createForOrder(
                id,
                1L,
                1L,
                1L
        );
    }

}
