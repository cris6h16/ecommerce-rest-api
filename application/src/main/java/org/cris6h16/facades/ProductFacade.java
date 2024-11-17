package org.cris6h16.facades;

import java.util.Set;

public interface ProductFacade {
    Long createProduct(CreateProductDTO dto);
    Set<CategoryDTO> getCategories(); //todo: pageable
    Long createCategory(CreateCategoryDTO dto);

    Long createBrand(CreateBrandDTO dto);
}
