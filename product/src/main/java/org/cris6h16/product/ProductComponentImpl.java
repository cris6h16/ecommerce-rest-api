package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.NotFound.ProductCategoryNotFoundException;
import org.cris6h16.product.Exceptions.NotFound.ProductUserNotFoundException;
import org.cris6h16.product.Exceptions.alreadyExists.ProductAlreadyExistsException;
import org.cris6h16.user.UserEntity;
import org.cris6h16.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class ProductComponentImpl implements ProductComponent {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    ProductComponentImpl(ProductRepository productRepository, ProductValidator productValidator, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Long createProduct(CreateProductInput input) {
        input.prepare();
        productValidator.validate(input);
        ProductEntity pe = toProductEntity(input);
        checkDuplicates(pe);
        return productRepository.save(pe).getId();
    }

    @Override
    public Long createCategory(CreateCategoryInput input) {
        productValidator.validate(input);
        CategoryEntity ce = toCategoryEntity(input);
        return categoryRepository.save(ce).getId();
    }

    private CategoryEntity toCategoryEntity(CreateCategoryInput input) {
        CategoryEntity ce = CategoryEntity.builder()
                .id(null)
                .name(input.getName())
                .build();
        log.info("to CategoryEntity: {}", ce);
        return ce;
    }

    @Override
    public Set<CategoryOutput> findAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::toCategoryOutput).toSet();
    }

    private CategoryOutput toCategoryOutput(CategoryEntity category) {
        return CategoryOutput.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private void checkDuplicates(ProductEntity pe) {
        boolean exists = productRepository.existsByNameAndApproxWeightLbAndApproxWidthCmAndApproxHeightCm(
                pe.getName(),
                pe.getApproxWeightLb(),
                pe.getApproxWidthCm(),
                pe.getApproxHeightCm()
        );
        if (exists) {
            throw new ProductAlreadyExistsException();
        }
    }

    private ProductEntity toProductEntity(CreateProductInput input) {
        ProductEntity pe = ProductEntity.builder()
                .id(null)
                .name(input.getName())
                .price(input.getPrice())
                .stock(input.getStock())
                .description(input.getDescription())
                .approxWeightLb(input.getApproxWeightLb())
                .approxWidthCm(input.getApproxWidthCm())
                .approxHeightCm(input.getApproxHeightCm())
                .imageUrl(input.getImageUrl())
                .category(findCategory(input.getCategoryId()))
                .user(findUser(input.getUserId()))
                .build();
        log.info("to ProductEntity: {}", pe);
        return pe;
    }

    private CategoryEntity findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(ProductCategoryNotFoundException::new);
    }


    private UserEntity findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(ProductUserNotFoundException::new);
    }


}
