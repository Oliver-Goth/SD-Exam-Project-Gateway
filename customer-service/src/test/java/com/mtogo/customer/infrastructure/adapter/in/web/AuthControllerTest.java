package com.mtogo.customer.infrastructure.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtogo.customer.application.exception.InvalidCredentialsException;
import com.mtogo.customer.domain.model.AccountStatus;
import com.mtogo.customer.domain.model.Address;
import com.mtogo.customer.domain.model.ContactInfo;
import com.mtogo.customer.domain.model.Customer;
import com.mtogo.customer.domain.model.VerificationStatus;
import com.mtogo.customer.domain.port.in.LoginUseCase;
import com.mtogo.customer.domain.port.in.RegisterCustomerUseCase;
import com.mtogo.customer.domain.port.in.VerifyCustomerUseCase;
import com.mtogo.customer.domain.port.out.TokenProviderPort;
import com.mtogo.customer.infrastructure.adapter.in.web.dto.LoginRequest;
import com.mtogo.customer.infrastructure.adapter.in.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("removal")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private RegisterCustomerUseCase registerCustomerUseCase;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private VerifyCustomerUseCase verifyCustomerUseCase;

    @MockBean
    private TokenProviderPort tokenProviderPort;

    // ---------------------------
    // TEST REGISTER ENDPOINT
    // ---------------------------
    @Test
    void testRegisterCustomerReturns201() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.fullName = "John Doe";
        request.email = "john@test.com";
        request.password = "password";
        request.phone = "12345678";
        request.street = "Main St";
        request.city = "City";
        request.postalCode = "1000";

        Customer mockCustomer = new Customer(
                1L,
                "John Doe",
                "john@test.com",
                "encodedPassword",
                AccountStatus.ACTIVE,
                VerificationStatus.PENDING,
                new ContactInfo("john@test.com", "12345678"),
                new Address("Main St", "City", "1000"),
                Collections.emptyList()
        );

        Mockito.when(registerCustomerUseCase.registerCustomer(any()))
                .thenReturn(mockCustomer);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    // ---------------------------
    // TEST LOGIN ENDPOINT
    // ---------------------------
    @Test
    void testLoginReturnsJwtToken() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("password");

        Mockito.when(loginUseCase.login(any(), any()))
                .thenReturn("jwt-token-example");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-example"));
    }

    @Test
    void testLoginInvalidCredentialsReturns401() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("john@test.com");
        request.setPassword("wrong");

        Mockito.when(loginUseCase.login(any(), any()))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ---------------------------
    // TEST VERIFY ENDPOINT
    // ---------------------------
    @Test
    void testVerifyCustomerReturns200() throws Exception {

        Mockito.doNothing().when(verifyCustomerUseCase).verify(eq("token123"));

        mockMvc.perform(get("/api/auth/verify")
                .param("token", "token123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account verified successfully!"));
    }
}
