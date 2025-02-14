package org.cris6h16.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    @Query("SELECT c FROM carts c WHERE c.userId = :userId")
    Optional<CartEntity> findByUserId(Long userId);
}
