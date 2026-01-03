package com.mtogo.delivery.persistence;

import com.mtogo.delivery.domain.model.DeliveryStatus;
import com.mtogo.delivery.infrastructure.adapter.out.persistence.DeliveryTaskJpaEntity;
import com.mtogo.delivery.infrastructure.adapter.out.persistence.DeliveryTaskJpaRepository;
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

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryMySQLContainerTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private DeliveryTaskJpaRepository repo;

    @Test
    void testSaveDeliveryInMySQL() {
        DeliveryTaskJpaEntity entity = new DeliveryTaskJpaEntity();
        entity.setDeliveryId(10L);
        entity.setAgentId(20L);
        entity.setOrderId(30L);
        entity.setDeliveryStatus(DeliveryStatus.ASSIGNED);

        DeliveryTaskJpaEntity saved = repo.save(entity);

        assertThat(saved.getDeliveryId()).isNotNull();
    }
}
