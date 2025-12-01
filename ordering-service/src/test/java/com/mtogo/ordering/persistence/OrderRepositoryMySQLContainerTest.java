package com.mtogo.ordering.persistence;

import com.mtogo.ordering.domain.model.OrderStatus;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderJpaEntity;
import com.mtogo.ordering.infrastructure.adapter.out.persistence.OrderJpaRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryMySQLContainerTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private OrderJpaRepository repo;

    @Test
    void testSaveOrderInMySQL() {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setCustomerId(4L);
        entity.setRestaurantId(6L);
        entity.setTotal(new BigDecimal("44.99"));
        entity.setStatus(OrderStatus.CREATED);

        OrderJpaEntity saved = repo.save(entity);

        assertThat(saved.getId()).isNotNull();
    }
}
