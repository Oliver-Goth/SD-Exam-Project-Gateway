package com.mtogo.restaurant.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<MenuJpaEntity, Long> {
    boolean existsByRestaurantId(Long restaurantId);
}
