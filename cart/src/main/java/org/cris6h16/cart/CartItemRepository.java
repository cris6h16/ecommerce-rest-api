package org.cris6h16.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    Set<CartItemEntity> findByCartId(Long cartId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE cart_items c SET c.quantity = :quantity WHERE c.id = :itemId")
    void updateQuantityById(Integer quantity, Long itemId);

    boolean existsByIdAndCartUserId(Long itemId, Long userId);

    boolean existsByProductIdAndCartUserId(Long productId, Long userId);

    @Query("SELECT c.productId FROM cart_items c WHERE c.id = :itemId")
    Optional<Long> findProductIdById(Long itemId);
}
