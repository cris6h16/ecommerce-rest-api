package org.cris6h16.facades;

import org.cris6h16.product.ProductOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

public interface ProductFacade {
    Long createProduct(CreateProductDTO dto);
    Set<CategoryDTO> getCategories(); //todo: pageable
    Long createCategory(CreateCategoryDTO dto);

    Page<ProductDTO> findAllProducts(Pageable pageable, Map<String, String> filters);

    Page<ProductDTO> findMyProducts(Pageable pageable);
}
