package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.ProductComponentAlreadyExistsException;
import org.cris6h16.product.Exceptions.ProductComponentInvalidAttributeException;
import org.cris6h16.product.Exceptions.ProductComponentNotFoundException;
import org.cris6h16.product.Exceptions.ProductErrorCode;
import org.cris6h16.user.UserEntity;
import org.cris6h16.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_NOT_FOUND_BY_ID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NOT_FOUND_BY_ID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.UNIQUE_USER_ID_PRODUCT_NAME;
import static org.cris6h16.product.Exceptions.ProductErrorCode.USER_NOT_FOUND_BY_ID;
import static org.cris6h16.product.ProductSpecs.hasNameLike;
import static org.cris6h16.product.ProductSpecs.hasPrice;

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
                .map(this::toProductOutputNotEager)
                .orElseThrow(() -> new ProductComponentNotFoundException(PRODUCT_NOT_FOUND_BY_ID));
    }

    @Override
    public Page<ProductOutput> findAllProducts(Pageable pageable, Map<String, String> filters) {
        if (filters.isEmpty()) {
            log.debug("No filters, returning all products(pageable: {})", pageable);
            return productRepository.findAll(pageable)
                    .map(this::toProductOutputNotEager);
        }

        Specification<ProductEntity> spec = createSpecification(filters);


        return productRepository.findAll(spec, pageable)
                .map(this::toProductOutputNotEager);
    }

    // jakarta.persistence.criteria.Root<ProductEntity>
    // jakarta.persistence.criteria.CriteriaQuery<?>
    // jakarta.persistence.criteria.CriteriaBuilder
    private Specification<ProductEntity> createSpecification(Map<String, String> filters) {
        Specification<ProductEntity> spec = Specification.where(null);

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String property = entry.getKey();
            String value = entry.getValue();
            log.debug("Creating filter specification for property: {} with value: {}", property, value);
            spec = spec.and(createFilterSpecification(property, value));
        }

        return spec;
    }

    private Specification<ProductEntity> createFilterSpecification(String property, String value) {
        if (property.equals("name")) {
            String[] split = Arrays.stream(value.split(" ")).map(String::trim).toArray(String[]::new);
            Specification<ProductEntity> spec = Specification.where(null);
            for (String s : split) {
                log.debug("Adding name filter: {}", s);
                spec = spec.and(hasNameLike(s));
            }

            return spec;
        }

        if (property.equals("price")) {
            return hasPrice(value);
        }

        log.error("Invalid filter attribute: {}", property);
        throw new ProductComponentInvalidAttributeException(ProductErrorCode.INVALID_FILTER_ATTRIBUTE);
    }


    @Override
    public Page<ProductOutput> findProductByUserId(Long userId, Pageable pageable) {
        productValidator.validateUserId(userId);
        return productRepository.findByUserId(userId, pageable)
                .map(this::toProductOutputNotEager);
    }

    private ProductOutput toProductOutputNotEager(ProductEntity productEntity) {
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
