package com.mtogo.delivery_service.controller;

import com.mtogo.delivery.domain.model.DeliveryTask;
import com.mtogo.delivery.domain.port.in.CreateDeliveryUseCase;
import com.mtogo.delivery.domain.port.in.UpdateDeliveryStatusUseCase;
import com.mtogo.delivery.infrastructure.api.DeliveryTaskController;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryTaskController.class)
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

        mvc.perform(post("/")
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
                .andExpect(jsonPath("$.deliveryStatus").value("PENDING"));
    }
    @Test
    void testAssignAgent() throws Exception{

    }

    @Test
    void testPickUp() throws Exception{}

    @Test
    void testDeliver() throws Exception{}

    private DeliveryTask sampleDelivery(Long id){
        return DeliveryTask.createForOrder(
                id,
                1L,
                1L,
                1L

        );
    }

}
