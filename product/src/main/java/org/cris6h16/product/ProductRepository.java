package org.cris6h16.product;

import org.cris6h16.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

 interface ProductRepository extends JpaRepository<ProductEntity, Long> {
//    boolean existsByNameAndApproxWeightLbAndApproxWidthCmAndApproxHeightCm(
//            String name,
//            Integer approxWeightLb,
//            Integer approxWidthCm,
//            Integer approxHeightCm
//    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE products p SET p.imageUrl = :url WHERE p.id = :id")
void updateImageUrlById(Long id, String url);

    boolean existsByNameAndUserId(String productName, Long userId);
}
