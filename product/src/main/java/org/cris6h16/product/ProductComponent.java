package org.cris6h16.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Set;

public interface ProductComponent {
    Long createProduct(CreateProductInput input);

    Long createCategory(CreateCategoryInput input);

    Set<CategoryOutput> findAllCategories(Pageable pageable);

    void updateImageUrlById(Long id, String url);

    void deleteAll();

    ProductOutput findProductByIdNoEager(Long productId);

    Page<ProductOutput> findAllProducts(Pageable pageable, Map<String, String> filters);

    Page<ProductOutput> findProductByUserId(Long userId, Pageable pageable);

//    BrandOutput getBrandByName(String name);
//    Long createBrand(CreateBrandInput input);
//    CategoryOutput getCategoryByName(String name);
//    Long createCategory(CreateCategoryInput input);
//
}
