package com.mtogo.customer.infrastructure.adapter.out.persistence;

import com.mtogo.customer.domain.model.Customer;
import com.mtogo.customer.domain.port.out.CustomerRepositoryPort;
import com.mtogo.customer.infrastructure.adapter.out.persistence.entity.CustomerJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerJpaRepository repository;

    public CustomerRepositoryAdapter(CustomerJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(CustomerMapper::mapToDomain);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return repository.findById(id)
                .map(CustomerMapper::mapToDomain);
    }

    @Override
    public Optional<Customer> findByVerificationToken(String token) {
        return repository.findByVerificationToken(token)
                .map(CustomerMapper::mapToDomain);
    }

    @Override
    public Customer save(Customer customer) {
        CustomerJpaEntity entity = CustomerMapper.mapToJpa(customer);
        CustomerJpaEntity saved = repository.save(entity);
        return CustomerMapper.mapToDomain(saved);
    }
}
