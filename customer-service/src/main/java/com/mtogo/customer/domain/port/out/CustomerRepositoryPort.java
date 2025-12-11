package com.mtogo.customer.domain.port.out;

import com.mtogo.customer.domain.model.Customer;

import java.util.Optional;

public interface CustomerRepositoryPort {

    Customer save(Customer customer);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findById(Long id);

    Optional<Customer> findByVerificationToken(String token);
}
