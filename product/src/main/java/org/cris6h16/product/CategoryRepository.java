package org.cris6h16.product;

import org.springframework.data.jpa.repository.JpaRepository;

 interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
