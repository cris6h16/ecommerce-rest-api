package org.cris6h16.facades;

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

    ProductDTO getProductById(Long id);

    void putProduct(Long id, CreateProductDTO createProductDTO);
}
