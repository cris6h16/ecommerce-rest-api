package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.ProductComponentAlreadyExistsException;
import org.cris6h16.product.Exceptions.ProductComponentNotFoundException;
import org.cris6h16.user.UserEntity;
import org.cris6h16.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_NOT_FOUND_BY_ID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NOT_FOUND_BY_ID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.UNIQUE_USER_ID_PRODUCT_NAME;
import static org.cris6h16.product.Exceptions.ProductErrorCode.USER_NOT_FOUND_BY_ID;
import static org.cris6h16.user.EntityMapper.toUserDTO;

@Slf4j
@Component
 class ProductComponentImpl implements ProductComponent {
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
        input.prepare();
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

    @Override
    public void updateImageUrlById(Long id, String url) {
        url = url == null ? "" : url.trim();

        productValidator.validateProductId(id);
        productValidator.validateImageUrl(url);

        productRepository.updateImageUrlById(id, url);
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Override
    public ProductOutput findProductByIdNoEager(Long productId) {
        productValidator.validateProductId(productId);
        return productRepository.findById(productId)
                .map(this::toProductOutputLazy)
                .orElseThrow(() -> new ProductComponentNotFoundException(PRODUCT_NOT_FOUND_BY_ID));
    }

    private ProductOutput toProductOutputLazy(ProductEntity productEntity) {
        return ProductOutput.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .stock(productEntity.getStock())
                .description(productEntity.getDescription())
                .approxWeightLb(productEntity.getApproxWeightLb())
                .approxWidthCm(productEntity.getApproxWidthCm())
                .approxHeightCm(productEntity.getApproxHeightCm())
                .imageUrl(productEntity.getImageUrl())
                .category(toCategoryOutput(productEntity.getCategory()))
                .user(null)
                .build();
    }


    private CategoryOutput toCategoryOutput(CategoryEntity category) {
        return CategoryOutput.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private void checkDuplicates(ProductEntity pe) {
        boolean exists = productRepository.existsByNameAndUserId(
                pe.getName(),
                pe.getUser().getId());

        if (exists) {
            throw new ProductComponentAlreadyExistsException(UNIQUE_USER_ID_PRODUCT_NAME); // todo: ponerlo el constraint a nivel de entidad
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
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProductComponentNotFoundException(CATEGORY_NOT_FOUND_BY_ID));
    }


    private UserEntity findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ProductComponentNotFoundException(USER_NOT_FOUND_BY_ID));
    }


}