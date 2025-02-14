package org.cris6h16.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

public interface ProductComponent {
    Long createProduct(CreateProductInput input);

    Long createCategory(CreateCategoryInput input);

    Set<CategoryOutput> findAllCategories(Pageable pageable);

    void updateImagesById(Long id, Set<String> url);

    void deleteAll();

    ProductOutput findProductByIdNoEager(Long productId);

    Page<ProductOutput> findAllProducts(Pageable pageable, Map<String, String> filters);

    Page<ProductOutput> findProductByUserId(Long userId, Pageable pageable);

    ProductOutput findProductById(Long id);

    void updateProductById(Long id, CreateProductInput input);

    boolean existProductById(Long id);

    boolean existProductByIdAndUserId(Long productId, Long userId);

    void deleteProductByIdAndUserId(Long productId, Long userId);

    CategoryOutput findCategoryById(Long categoryId);

    Integer findProductStockById(Long productId);


//    BrandOutput getBrandByName(String name);
//    Long createBrand(CreateBrandInput input);
//    CategoryOutput getCategoryByName(String name);
//    Long createCategory(CreateCategoryInput input);
//
}
