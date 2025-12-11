package com.mtogo.customer.infrastructure.config;

import com.mtogo.customer.application.service.CustomerApplicationService;
import com.mtogo.customer.domain.port.in.LoginUseCase;
import com.mtogo.customer.domain.port.in.RegisterCustomerUseCase;
import com.mtogo.customer.domain.port.in.VerifyCustomerUseCase;
import com.mtogo.customer.domain.port.out.CustomerRepositoryPort;
import com.mtogo.customer.domain.port.out.PasswordEncoderPort;
import com.mtogo.customer.domain.port.out.TokenProviderPort;
import com.mtogo.customer.domain.port.out.EmailVerificationPort;
import com.mtogo.customer.infrastructure.adapter.out.messaging.InMemoryVerificationStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CustomerConfig {

    @Bean
    @Primary
    public CustomerApplicationService customerApplicationService(
            CustomerRepositoryPort customerRepositoryPort,
            PasswordEncoderPort passwordEncoderPort,
            TokenProviderPort tokenProviderPort,
            EmailVerificationPort emailVerificationPort,
            com.mtogo.customer.domain.port.out.CustomerEventPublisherPort eventPublisherPort,
            InMemoryVerificationStore verificationStore
    ) {
        return new CustomerApplicationService(
                customerRepositoryPort,
                passwordEncoderPort,
                tokenProviderPort,
                emailVerificationPort,
                eventPublisherPort,
                verificationStore
        );
    }

    @Bean(autowireCandidate = false)
    public RegisterCustomerUseCase registerCustomerUseCase(CustomerApplicationService service) {
        return service;
    }

    @Bean(autowireCandidate = false)
    public LoginUseCase loginUseCase(CustomerApplicationService service) {
        return service;
    }

    @Bean(autowireCandidate = false)
    public VerifyCustomerUseCase verifyCustomerUseCase(CustomerApplicationService service) {
        return service;
    }
}
