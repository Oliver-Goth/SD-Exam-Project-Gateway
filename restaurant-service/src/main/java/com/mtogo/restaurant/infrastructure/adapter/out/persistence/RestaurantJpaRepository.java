package com.mtogo.restaurant.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, Long> {
    Optional<RestaurantJpaEntity> findByEmail(String email);
}
