package com.mtogo.customer.application.service;

import com.mtogo.customer.domain.model.AccountStatus;
import com.mtogo.customer.domain.model.Address;
import com.mtogo.customer.domain.model.ContactInfo;
import com.mtogo.customer.domain.model.Customer;
import com.mtogo.customer.domain.model.VerificationStatus;
import com.mtogo.customer.domain.port.in.GetCustomerProfileUseCase;
import com.mtogo.customer.domain.port.in.LoginUseCase;
import com.mtogo.customer.domain.port.in.RegisterCustomerCommand;
import com.mtogo.customer.domain.port.in.RegisterCustomerUseCase;
import com.mtogo.customer.domain.port.in.UpdateCustomerProfileCommand;
import com.mtogo.customer.domain.port.in.UpdateCustomerProfileUseCase;
import com.mtogo.customer.domain.port.in.VerifyCustomerUseCase;
import com.mtogo.customer.domain.port.out.CustomerRepositoryPort;
import com.mtogo.customer.domain.port.out.EmailVerificationPort;
import com.mtogo.customer.domain.port.out.PasswordEncoderPort;
import com.mtogo.customer.domain.port.out.CustomerEventPublisherPort;
import com.mtogo.customer.domain.port.out.TokenProviderPort;
import com.mtogo.customer.application.exception.CustomerNotFoundException;
import com.mtogo.customer.application.exception.CustomerNotVerifiedException;
import com.mtogo.customer.application.exception.EmailAlreadyUsedException;
import com.mtogo.customer.application.exception.InvalidCredentialsException;
import com.mtogo.customer.application.exception.InvalidVerificationTokenException;
import com.mtogo.customer.infrastructure.adapter.out.messaging.InMemoryVerificationStore;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class CustomerApplicationService
        implements RegisterCustomerUseCase, LoginUseCase, VerifyCustomerUseCase,
        GetCustomerProfileUseCase, UpdateCustomerProfileUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;
    private final EmailVerificationPort emailVerificationPort;
    private final CustomerEventPublisherPort eventPublisherPort;
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring injects a singleton store bean")
    private final InMemoryVerificationStore verificationStore;

    public CustomerApplicationService(CustomerRepositoryPort customerRepositoryPort,
                                      PasswordEncoderPort passwordEncoderPort,
                                      TokenProviderPort tokenProviderPort,
                                      EmailVerificationPort emailVerificationPort,
                                      CustomerEventPublisherPort eventPublisherPort,
                                      InMemoryVerificationStore verificationStore) {
        this.customerRepositoryPort = customerRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
        this.emailVerificationPort = emailVerificationPort;
        this.eventPublisherPort = eventPublisherPort;
        this.verificationStore = verificationStore;
    }

    // ------------------------------------------------------------
    //  Use Case: Register Customer
    // ------------------------------------------------------------
    @Override
    public Customer registerCustomer(RegisterCustomerCommand command) {

        // 1) Check if email already exists
        customerRepositoryPort.findByEmail(command.email)
                .ifPresent(existing -> {
                    throw new EmailAlreadyUsedException(command.email);
                });

        // 2) Encode password
        String encodedPassword = passwordEncoderPort.encode(command.rawPassword);

        // 3) Create value objects
        ContactInfo contactInfo = new ContactInfo(command.email, command.phone);
        Address address = new Address(command.street, command.city, command.postalCode);

        // 4) Create Customer aggregate (domain object)
        Customer customer = new Customer(
                null,
                command.fullName,
                command.email,
                encodedPassword,
                AccountStatus.ACTIVE,
                VerificationStatus.PENDING,
                contactInfo,
                address,
                Collections.emptyList()
        );

        // 5) Generate verification token and set on aggregate
        String verificationToken = java.util.UUID.randomUUID().toString();
        customer.setVerificationToken(verificationToken);

        // 6) Save through repository port
        Customer saved = customerRepositoryPort.save(customer);

        // 7) Send verification email through outbound port
        emailVerificationPort.sendVerification(saved.getEmail(), verificationToken);
        eventPublisherPort.publishCustomerRegistered(saved.getCustomerId(), saved.getEmail());

        return saved;
    }

    // ------------------------------------------------------------
    //  Use Case: Login (returns JWT)
    // ------------------------------------------------------------
    @Override
    public String login(String email, String rawPassword) {

        // 1) Find customer by email
        Customer customer = customerRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // 2) Check password
        if (!passwordEncoderPort.matches(rawPassword, customer.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // 3) Check verification status
        if (customer.getVerificationStatus() != VerificationStatus.VERIFIED) {
            throw new CustomerNotVerifiedException("Account is not verified");
        }

        // 4) Generate JWT token
        return tokenProviderPort.generateToken(customer.getCustomerId());
    }

    // ------------------------------------------------------------
    //  Use Case: Verify Customer (e.g. from email link)
    // ------------------------------------------------------------
    @Override
    public boolean verifyCustomer(Long customerId, String token) {
        String storedToken = verificationStore.getToken(customerId);

        if (storedToken == null || !storedToken.equals(token)) {
            throw new InvalidVerificationTokenException();
        }

        Customer customer = customerRepositoryPort.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        customer.verifyAccount();

        customerRepositoryPort.save(customer);

        verificationStore.removeToken(customerId);

        return true;
    }

    @Override
    public void verify(String token) {
        Customer customer = customerRepositoryPort.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        customer.verifyAccount();
        customer.setVerificationToken(null);

        customerRepositoryPort.save(customer);
        eventPublisherPort.publishCustomerVerified(customer.getCustomerId(), customer.getEmail());
    }

    @Override
    public Customer getProfile(Long customerId) {
        return customerRepositoryPort.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Customer updateProfile(Long customerId, UpdateCustomerProfileCommand command) {
        Customer customer = customerRepositoryPort.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.updateFullName(command.fullName());
        customer.updateContactPhone(command.phone());
        customer.updateAddress(command.street(), command.city(), command.postalCode());

        return customerRepositoryPort.save(customer);
    }
}
