package com.mtogo.delivery.persistence;

import com.mtogo.delivery.domain.model.DeliveryStatus;
import com.mtogo.delivery.infrastructure.adapter.out.persistence.DeliveryTaskJpaEntity;
import com.mtogo.delivery.infrastructure.adapter.out.persistence.DeliveryTaskJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeliveryRepositoryH2Test {

    @Autowired
    private DeliveryTaskJpaRepository repo;

    @Test
    void testSaveDelivery() {
        DeliveryTaskJpaEntity entity = new DeliveryTaskJpaEntity();
        entity.setDeliveryId(1L);
        entity.setAgentId(2L);
        entity.setOrderId(3L);
        entity.setDeliveryStatus(DeliveryStatus.ASSIGNED);

        DeliveryTaskJpaEntity saved = repo.save(entity);

        assertThat(saved.getDeliveryId()).isNotNull();
    }
}
