package com.mtogo.customer.infrastructure.adapter.out.persistence;

import com.mtogo.customer.infrastructure.adapter.out.persistence.entity.CustomerJpaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerJpaRepository repo;

    @Test
    void testSaveAndFindCustomer() {
        CustomerJpaEntity entity = new CustomerJpaEntity(
                null,
                "John Doe",
                "john@test.com",
                "123",
                "ACTIVE",
                "PENDING",
                null,
                "12345678",
                "Main St",
                "City",
                "1000"
        );

        CustomerJpaEntity saved = repo.save(entity);

        assertNotNull(saved.getId());

        CustomerJpaEntity found = repo.findByEmail("john@test.com").orElse(null);

        assertNotNull(found);
        assertEquals("John Doe", found.getFullName());
    }

    @Test
    void testUpdateCustomer() {
        CustomerJpaEntity entity = new CustomerJpaEntity(
                null,
                "John Doe",
                "john@test.com",
                "123",
                "ACTIVE",
                "PENDING",
                null,
                "12345678",
                "Main St",
                "City",
                "1000"
        );

        CustomerJpaEntity saved = repo.save(entity);
        assertNotNull(saved.getId());

        CustomerJpaEntity updated = new CustomerJpaEntity(
                saved.getId(),
                "Johnny Updated",
                saved.getEmail(),
                saved.getPassword(),
                "SUSPENDED",
                saved.getVerificationStatus(),
                saved.getVerificationToken(),
                saved.getPhone(),
                "New Street",
                saved.getCity(),
                saved.getPostalCode()
        );

        repo.save(updated);

        CustomerJpaEntity found = repo.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Johnny Updated", found.getFullName());
        assertEquals("SUSPENDED", found.getAccountStatus());
        assertEquals("New Street", found.getStreet());
    }

    @Test
    void testDeleteCustomer() {
        CustomerJpaEntity entity = new CustomerJpaEntity(
                null,
                "Jane Doe",
                "jane@test.com",
                "123",
                "ACTIVE",
                "PENDING",
                null,
                "87654321",
                "Second St",
                "City",
                "2000"
        );

        CustomerJpaEntity saved = repo.save(entity);
        assertNotNull(saved.getId());

        repo.delete(saved);

        assertFalse(repo.findById(saved.getId()).isPresent());
        assertTrue(repo.findByEmail("jane@test.com").isEmpty());
    }
}
