package com.mtogo.customer.domain.port.out;

public interface CustomerEventPublisherPort {

    void publishCustomerRegistered(Long customerId, String email);

    void publishCustomerVerified(Long customerId, String email);
}
