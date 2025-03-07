package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.ProductComponentException;
import org.cris6h16.product.Exceptions.ProductErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_NOT_FOUND;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NOT_FOUND;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NOT_FOUND_BY_ID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.UNIQUE_USER_ID_PRODUCT_NAME;
import static org.cris6h16.product.ProductSpecs.hasCategoryId;
import static org.cris6h16.product.ProductSpecs.hasDescriptionLike;
import static org.cris6h16.product.ProductSpecs.hasNameLike;
import static org.cris6h16.product.ProductSpecs.hasPrice;

@Component
@Slf4j
class ProductComponentImpl implements ProductComponent {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final CategoryRepository categoryRepository;

    ProductComponentImpl(ProductRepository productRepository, ProductValidator productValidator,CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void updateProductById(Long id, CreateProductInput input) {
        input.prepare();
        productValidator.validate(input);
        productValidator.validateProductId(id);

        ProductEntity pe = toProductEntity(input);
        pe.setId(id);
        checkDuplicates(pe, id);

        productRepository.save(pe);
    }

    @Override
    public Long createProduct(CreateProductInput input) {
        input.prepare();
        productValidator.validate(input);

        categoryExists(input.getCategoryId());

        ProductEntity pe = toProductEntity(input);
        checkDuplicates(pe, null);

        return productRepository.save(pe).getId();
    }

    private void categoryExists(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ProductComponentException(CATEGORY_NOT_FOUND);
        }
    }

    @Override
    public Long createCategory(CreateCategoryInput input) {
        input.prepare();
        productValidator.validate(input);
        CategoryEntity ce = toCategoryEntity(input);
        return categoryRepository.save(ce).getId();
    }

    private CategoryEntity toCategoryEntity(CreateCategoryInput input) {
        return CategoryEntity.builder()
                .id(null)
                .name(input.getName())
                .build();
    }

    @Override
    public Set<CategoryOutput> findAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::toCategoryOutput).toSet();
    }

    @Override
    public void updateImagesById(Long id, Set<String> urls) {
        urls = productValidator.validateImagesUrl(urls);
        productValidator.validateProductId(id);

        ProductEntity pe = productRepository.findById(id).orElseThrow(() -> new ProductComponentException(PRODUCT_NOT_FOUND_BY_ID));
        pe.setImageUrls(urls);
        productRepository.save(pe);
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
                .orElseThrow(() -> new ProductComponentException(PRODUCT_NOT_FOUND_BY_ID));
    }

    @Override
    public Page<ProductOutput> findAllProducts(Pageable pageable, Map<String, String> filters) {
        if (filters.isEmpty()) {
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
            spec = spec.and(createFilterSpecification(property, value));
        }

        return spec;
    }

    private Specification<ProductEntity> createFilterSpecification(String property, String value) {
        // todo: escapar caracteres especiales
        return switch (property) {
            case "query" -> {
                String[] words = Arrays.stream(value.split(" ")).map(String::trim).toArray(String[]::new);
                Specification<ProductEntity> spec = Specification.where(null);
                for (String word : words) {
                    spec = spec.or(Specification.where(hasNameLike(word).or(hasDescriptionLike(word))));
                }

                yield spec;
            }
            case "price" -> {
                Specification<ProductEntity> spec = Specification.where(null);
                for (String p : value.split(",")) {
                    p = p.trim();
                    spec = spec.and(hasPrice(p));
                }

                yield spec;
            }
            case "categoryId" -> hasCategoryId(value);
            default -> throw new ProductComponentException(ProductErrorCode.UNSUPPORTED_FILTER_ATTRIBUTE);
        };
    }


    @Override
    public Page<ProductOutput> findProductByUserId(Long userId, Pageable pageable) {
        productValidator.validateUserId(userId);
        return productRepository.findByUserId(userId, pageable)
                .map(this::toProductOutputNotEager);
    }

    @Override
    public ProductOutput findProductById(Long id) {
        productValidator.validateProductId(id);
        return productRepository.findById(id)
                .map(this::toProductOutput)
                .orElseThrow(() -> new ProductComponentException(PRODUCT_NOT_FOUND_BY_ID));
    }


    @Override
    public boolean existProductById(Long id) {
        return false;
    }

    @Override
    public boolean existProductByIdAndUserId(Long productId, Long userId) {
        productValidator.validateProductId(productId);
        productValidator.validateUserId(userId);

        return this.productRepository.existsByIdAndUserId(productId, userId);
    }

    @Override
    public void deleteProductByIdAndUserId(Long productId, Long userId) {
        productValidator.validateProductId(productId);
        productValidator.validateUserId(userId);

        productRepository.deleteByIdAndUserId(productId, userId);
    }

    @Override
    public CategoryOutput findCategoryById(Long categoryId) {
        productValidator.validateCategoryId(categoryId);
        return categoryRepository.findById(categoryId)
                .map(this::toCategoryOutput)
                .orElseThrow(() -> new ProductComponentException(CATEGORY_NOT_FOUND));
    }

    @Override
    public Integer findProductStockById(Long productId) {
        return productRepository
                .findStockById(productId)
                .orElseThrow(()-> new ProductComponentException(PRODUCT_NOT_FOUND));
    }

    private ProductOutput toProductOutput(ProductEntity p) {
        return ProductOutput.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .stock(p.getStock())
                .description(p.getDescription())
                .weightPounds(p.getWeightPounds())
                .widthCM(p.getWidthCM())
                .heightCM(p.getHeightCM())
                .imageUrls(p.getImageUrls())
                .categoryId(p.getCategoryId())
                .userId(p.getUserId())
                .build();
    }

    private ProductOutput toProductOutputNotEager(ProductEntity p) {
        return ProductOutput.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .stock(p.getStock())
                .description(p.getDescription())
                .weightPounds(p.getWeightPounds())
                .widthCM(p.getWidthCM())
                .heightCM(p.getHeightCM())
                .imageUrls(p.getImageUrls())
                .categoryId(p.getCategoryId())
                .userId(p.getUserId())
                .build();
    }


    private CategoryOutput toCategoryOutput(CategoryEntity category) {
        return CategoryOutput.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private void checkDuplicates(ProductEntity pe, Long productId) {
        // ? creating : updating
        boolean exists = (productId == null)
                ? productRepository.existsByNameAndUserId(pe.getName(), pe.getUserId())
                : productRepository.existsByNameAndUserIdAndIdNot(pe.getName(), pe.getUserId(), productId);

        if (exists) throw new ProductComponentException(UNIQUE_USER_ID_PRODUCT_NAME);
    }


    private ProductEntity toProductEntity(CreateProductInput input) {
        return ProductEntity.builder()
                .id(null)
                .name(input.getName())
                .price(input.getPrice())
                .stock(input.getStock())
                .description(input.getDescription())
                .weightPounds(input.getWeightPounds())
                .widthCM(input.getWidthCM())
                .heightCM(input.getHeightCM())
                .lengthCM(input.getLengthCM())
                .imageUrls(input.getImageUrls())
                .categoryId(input.getCategoryId())
                .userId(input.getUserId())
                .build();
    }

}
