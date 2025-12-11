package com.mtogo.customer.infrastructure.adapter.in.web;

import com.mtogo.customer.domain.model.AccountStatus;
import com.mtogo.customer.domain.model.Address;
import com.mtogo.customer.domain.model.ContactInfo;
import com.mtogo.customer.domain.model.Customer;
import com.mtogo.customer.domain.model.VerificationStatus;
import com.mtogo.customer.domain.port.in.GetCustomerProfileUseCase;
import com.mtogo.customer.domain.port.in.UpdateCustomerProfileUseCase;
import com.mtogo.customer.domain.port.out.TokenProviderPort;
import com.mtogo.customer.infrastructure.adapter.out.security.JwtAuthenticationFilter;
import com.mtogo.customer.infrastructure.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetCustomerProfileUseCase getCustomerProfileUseCase;

    @MockBean
    private UpdateCustomerProfileUseCase updateCustomerProfileUseCase;

    @MockBean
    private TokenProviderPort tokenProviderPort;

    @Test
    void getMyProfileReturnsCustomer() throws Exception {
        Customer customer = new Customer(
                42L,
                "Jane Doe",
                "jane@test.com",
                "pw",
                AccountStatus.ACTIVE,
                VerificationStatus.VERIFIED,
                new ContactInfo("jane@test.com", "12345678"),
                new Address("Street", "City", "0000"),
                Collections.emptyList()
        );

        when(getCustomerProfileUseCase.getProfile(42L)).thenReturn(customer);
        when(tokenProviderPort.validateAndGetUserId("good-token")).thenReturn(42L);

        mockMvc.perform(get("/customers/me")
                        .header("Authorization", "Bearer good-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@test.com"));
    }

    @Test
    void updateMyProfileReturnsUpdatedCustomer() throws Exception {
        Customer updated = new Customer(
                42L,
                "Updated Name",
                "jane@test.com",
                "pw",
                AccountStatus.ACTIVE,
                VerificationStatus.VERIFIED,
                new ContactInfo("jane@test.com", "99999999"),
                new Address("New Street", "New City", "1234"),
                Collections.emptyList()
        );

        when(tokenProviderPort.validateAndGetUserId("good-token")).thenReturn(42L);
        when(updateCustomerProfileUseCase.updateProfile(org.mockito.ArgumentMatchers.eq(42L), org.mockito.ArgumentMatchers.any()))
                .thenReturn(updated);

        String payload = """
                {
                  "fullName": "Updated Name",
                  "phone": "99999999",
                  "street": "New Street",
                  "city": "New City",
                  "postalCode": "1234"
                }
                """;

        mockMvc.perform(put("/customers/me")
                        .header("Authorization", "Bearer good-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Updated Name"))
                .andExpect(jsonPath("$.contactInfo.phone").value("99999999"))
                .andExpect(jsonPath("$.address.street").value("New Street"));
    }
}
