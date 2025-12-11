package com.mtogo.customer.infrastructure.adapter.out.persistence;

import com.mtogo.customer.infrastructure.adapter.out.persistence.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, Long> {

    Optional<CustomerJpaEntity> findByEmail(String email);

    Optional<CustomerJpaEntity> findByVerificationToken(String token);
}
