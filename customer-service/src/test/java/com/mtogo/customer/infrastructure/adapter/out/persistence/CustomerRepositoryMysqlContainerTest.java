package com.mtogo.customer.infrastructure.adapter.out.persistence;

import com.mtogo.customer.infrastructure.adapter.out.persistence.entity.CustomerJpaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryMysqlContainerTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("customer_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private CustomerJpaRepository repo;

    @Test
    void testSaveAndLoadCustomer() {
        CustomerJpaEntity entity = new CustomerJpaEntity(
                null,
                "Maria",
                "maria@test.com",
                "pass123",
                "ACTIVE",
                "VERIFIED",
                null,
                "22233344",
                "Street 10",
                "City",
                "12345"
        );

        CustomerJpaEntity saved = repo.save(entity);

        assertNotNull(saved.getId());

        CustomerJpaEntity found = repo.findByEmail("maria@test.com").orElse(null);

        assertNotNull(found);
        assertEquals("Maria", found.getFullName());
        assertEquals("VERIFIED", found.getVerificationStatus());
    }
}
