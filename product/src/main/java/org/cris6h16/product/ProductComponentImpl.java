package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.NotFound.ProductBrandNotFoundException;
import org.cris6h16.product.Exceptions.NotFound.ProductCategoryNotFoundException;
import org.cris6h16.product.Exceptions.NotFound.ProductUserNotFoundException;
import org.cris6h16.product.Exceptions.alreadyExists.ProductAlreadyExistsException;
import org.cris6h16.user.UserEntity;
import org.cris6h16.user.UserRepository;

@Slf4j
public class ProductComponentImpl implements ProductComponent {
    private final ProductRepository productRepository;
    private final ProductValidator ProductValidator;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

     ProductComponentImpl(ProductRepository productRepository, ProductValidator productValidator, UserRepository userRepository, BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.ProductValidator = productValidator;
        this.userRepository = userRepository;
        this.brandRepository = brandRepository;
         this.categoryRepository = categoryRepository;
     }

    @Override
    public Long save(CreateProductInput input) {
        input.prepare();
        ProductValidator.validate(input);
        ProductEntity pe = createProductEntity(input);
        checkDuplicates(pe);
        return productRepository.save(pe).getId();
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

    private ProductEntity createProductEntity(CreateProductInput input) {
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
                .brand(findBrand(input.getBrandId()))
                .build();
        log.info("ProductEntity created: {}", pe);
        return pe;
    }

    private CategoryEntity findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(ProductCategoryNotFoundException::new);
    }

    private BrandEntity findBrand(Long brandId) {
        return brandRepository.findById(brandId).orElseThrow(ProductBrandNotFoundException::new);
    }

    private UserEntity findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(ProductUserNotFoundException::new);
    }


}
