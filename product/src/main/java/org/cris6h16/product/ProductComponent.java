package org.cris6h16.product;

import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ProductComponent {
    Long createProduct(CreateProductInput input);

    Long createCategory(CreateCategoryInput input);

    Set<CategoryOutput> findAllCategories(Pageable pageable);

//    BrandOutput getBrandByName(String name);
//    Long createBrand(CreateBrandInput input);
//    CategoryOutput getCategoryByName(String name);
//    Long createCategory(CreateCategoryInput input);
//
}
