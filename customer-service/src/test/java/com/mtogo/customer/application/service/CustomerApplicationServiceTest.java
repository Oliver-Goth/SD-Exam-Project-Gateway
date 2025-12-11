package com.mtogo.customer.application.service;

import com.mtogo.customer.application.exception.CustomerNotVerifiedException;
import com.mtogo.customer.application.exception.EmailAlreadyUsedException;
import com.mtogo.customer.application.exception.InvalidCredentialsException;
import com.mtogo.customer.application.exception.InvalidVerificationTokenException;
import com.mtogo.customer.domain.model.*;
import com.mtogo.customer.domain.port.in.RegisterCustomerCommand;
import com.mtogo.customer.domain.port.out.CustomerRepositoryPort;
import com.mtogo.customer.domain.port.out.EmailVerificationPort;
import com.mtogo.customer.domain.port.out.PasswordEncoderPort;
import com.mtogo.customer.domain.port.out.TokenProviderPort;
import com.mtogo.customer.infrastructure.adapter.out.messaging.InMemoryVerificationStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class CustomerApplicationServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepositoryPort;
    @Mock
    private PasswordEncoderPort passwordEncoderPort;
    @Mock
    private TokenProviderPort tokenProviderPort;
    @Mock
    private EmailVerificationPort emailVerificationPort;
    @Mock
    private com.mtogo.customer.domain.port.out.CustomerEventPublisherPort eventPublisherPort;
    @Mock
    private InMemoryVerificationStore verificationStore;

    private CustomerApplicationService service;

    @BeforeEach
    void setUp() {
        service = new CustomerApplicationService(
                customerRepositoryPort,
                passwordEncoderPort,
                tokenProviderPort,
                emailVerificationPort,
                eventPublisherPort,
                verificationStore
        );
    }

    @Test
    void registerCustomerSendsEmailAndReturnsSavedAggregate() {
        RegisterCustomerCommand cmd = new RegisterCustomerCommand();
        cmd.fullName = "Test User";
        cmd.email = "test@example.com";
        cmd.rawPassword = "secret";
        cmd.phone = "12345678";
        cmd.street = "Street";
        cmd.city = "City";
        cmd.postalCode = "0000";

        when(customerRepositoryPort.findByEmail(cmd.email)).thenReturn(Optional.empty());
        when(passwordEncoderPort.encode(cmd.rawPassword)).thenReturn("encoded");

        Customer saved = createCustomer(1L, cmd.email, VerificationStatus.PENDING);
        when(customerRepositoryPort.save(any(Customer.class))).thenReturn(saved);

        Customer result = service.registerCustomer(cmd);

        assertEquals(saved, result);
        verify(emailVerificationPort).sendVerification(eq(saved.getEmail()), anyString());
    }

    @Test
    void registerCustomerThrowsWhenEmailExists() {
        RegisterCustomerCommand cmd = new RegisterCustomerCommand();
        cmd.email = "used@example.com";
        when(customerRepositoryPort.findByEmail(cmd.email)).thenReturn(Optional.of(createCustomer(2L, cmd.email, VerificationStatus.VERIFIED)));

        assertThrows(EmailAlreadyUsedException.class, () -> service.registerCustomer(cmd));
        verify(customerRepositoryPort, never()).save(any());
    }

    @Test
    void verifyCustomerSucceedsWithMatchingToken() {
        Long customerId = 1L;
        Customer customer = createCustomer(customerId, "verify@example.com", VerificationStatus.PENDING);

        when(verificationStore.getToken(customerId)).thenReturn("abc");
        when(customerRepositoryPort.findById(customerId)).thenReturn(Optional.of(customer));

        boolean result = service.verifyCustomer(customerId, "abc");

        assertTrue(result);
        assertEquals(VerificationStatus.VERIFIED, customer.getVerificationStatus());
        verify(customerRepositoryPort).save(customer);
        verify(verificationStore).removeToken(customerId);
    }

    @Test
    void verifyCustomerThrowsOnInvalidToken() {
        when(verificationStore.getToken(1L)).thenReturn("abc");
        assertThrows(InvalidVerificationTokenException.class, () -> service.verifyCustomer(1L, "wrong"));
        verify(customerRepositoryPort, never()).save(any());
    }

    @Test
    void verifyByTokenMarksCustomerVerifiedAndClearsToken() {
        Customer customer = createCustomer(1L, "verify@example.com", VerificationStatus.PENDING);
        customer.setVerificationToken("tok");

        when(customerRepositoryPort.findByVerificationToken("tok")).thenReturn(Optional.of(customer));

        service.verify("tok");

        assertEquals(VerificationStatus.VERIFIED, customer.getVerificationStatus());
        assertNull(customer.getVerificationToken());
        verify(customerRepositoryPort).save(customer);
    }

    @Test
    void loginReturnsTokenForVerifiedCustomer() {
        Customer customer = createCustomer(1L, "login@example.com", VerificationStatus.VERIFIED);
        when(customerRepositoryPort.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(passwordEncoderPort.matches("pw", customer.getPassword())).thenReturn(true);
        when(tokenProviderPort.generateToken(customer.getCustomerId())).thenReturn("jwt-token");

        String token = service.login(customer.getEmail(), "pw");

        assertEquals("jwt-token", token);
    }

    @Test
    void loginThrowsWhenNotVerified() {
        Customer customer = createCustomer(1L, "pending@example.com", VerificationStatus.PENDING);
        when(customerRepositoryPort.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(passwordEncoderPort.matches("pw", customer.getPassword())).thenReturn(true);

        assertThrows(CustomerNotVerifiedException.class, () -> service.login(customer.getEmail(), "pw"));
    }

    @Test
    void loginThrowsOnBadCredentials() {
        Customer customer = createCustomer(1L, "wrongpw@example.com", VerificationStatus.VERIFIED);
        when(customerRepositoryPort.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(passwordEncoderPort.matches("pw", customer.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> service.login(customer.getEmail(), "pw"));
    }

    @Test
    void loginThrowsWhenEmailNotFound() {
        when(customerRepositoryPort.findByEmail("missing@example.com")).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class, () -> service.login("missing@example.com", "pw"));
    }

    private Customer createCustomer(Long id, String email, VerificationStatus verificationStatus) {
        return new Customer(
                id,
                "User",
                email,
                "hashed",
                AccountStatus.ACTIVE,
                verificationStatus,
                new ContactInfo(email, "12345678"),
                new Address("Street", "City", "0000"),
                Collections.emptyList()
        );
    }
}
