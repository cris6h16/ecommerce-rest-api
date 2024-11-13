package org.cris6h16.product;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByNameAndApproxWeightLbAndApproxWidthCmAndApproxHeightCm(
            String name,
            Integer approxWeightLb,
            Integer approxWidthCm,
            Integer approxHeightCm
    );
}
