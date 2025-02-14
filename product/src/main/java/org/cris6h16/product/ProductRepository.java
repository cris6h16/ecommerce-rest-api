package org.cris6h16.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

 interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    boolean existsByNameAndUserId(String productName, Long userId);

    Page<ProductEntity> findByUserId(Long userId, Pageable pageable);

    Page<ProductEntity> findAll(Specification<ProductEntity> spec, Pageable pageable);

    @Query("SELECT COUNT(p) > 0 FROM products p WHERE p.id != :productId AND p.name = :name AND p.userId = :userId")
    boolean existsByNameAndUserIdAndIdNot(String name, Long userId, Long productId);

    boolean existsByIdAndUserId(Long productId, Long userId);

    void deleteByIdAndUserId(Long productId, Long userId);

    Optional<Integer> findStockById(Long productId);
 }
