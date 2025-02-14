package org.cris6h16.facades;

import org.cris6h16.facades.Exceptions.ApplicationErrorCode;
import org.cris6h16.facades.Exceptions.ApplicationException;
import org.cris6h16.file.FileComponent;
import org.cris6h16.product.CategoryOutput;
import org.cris6h16.product.CreateCategoryInput;
import org.cris6h16.product.CreateProductInput;
import org.cris6h16.product.ProductComponent;
import org.cris6h16.product.ProductOutput;
import org.cris6h16.security.SecurityComponent;
import org.cris6h16.user.UserComponent;
import org.cris6h16.user.UserOutput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.FORBIDDEN_SORT_PROPERTY;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.PAGE_SIZE_TOO_BIG;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.PRODUCT_NOT_FOUND_BY_ID;

@Component // todo: should be a custom annotation @Facade -> @Service
public class ProductFacadeImpl implements ProductFacade {
    private final ProductComponent productComponent;
    private final SecurityComponent securityComponent;
    private final FileComponent fileComponent;
    private final UserComponent userComponent;
    private static final long MAX_IMG_SIZE = 5 * 1024 * 1024;


    public ProductFacadeImpl(ProductComponent productComponent, SecurityComponent securityComponent, FileComponent fileComponent, UserComponent userComponent) {
        this.productComponent = productComponent;
        this.securityComponent = securityComponent;
        this.fileComponent = fileComponent;
        this.userComponent = userComponent;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public Long createProduct(CreateProductDTO dto) {
        Long userId = securityComponent.getCurrentUserId();
        isEmailVerified(userId);
        CreateProductInput input = toInput(dto, userId);
        defImgIfEmpty(input);
        Long id = productComponent.createProduct(input);
        Set<String> url = fileComponent.uploadImages(dto.getImages(), MAX_IMG_SIZE);
        productComponent.updateImagesById(id, url);
        return id;
    }

    private void defImgIfEmpty(CreateProductInput input) {
        if (input.getImageUrls().isEmpty()) {
            input.getImageUrls().add("https://via.placeholder.com/150");
        }
    }

    private void isEmailVerified(Long userId) {
        boolean isEmailVerified = userComponent.findEmailVerifiedById(true, userId);
        if (isEmailVerified) return;
        throw new ApplicationException(ApplicationErrorCode.EMAIL_NOT_VERIFIED);
    }

    @Override
    public Set<CategoryDTO> getCategories() {
        return productComponent.findAllCategories(Pageable.unpaged()).stream()
                .map(this::toDTO)
                .collect(toSet());
    }

    private CategoryDTO toDTO(CategoryOutput output) {
        if (output == null) return CategoryDTO.builder().build();
        return CategoryDTO.builder()
                .id(output.getId())
                .name(output.getName())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public Long createCategory(CreateCategoryDTO dto) {
        Long userId = securityComponent.getCurrentUserId();
        return productComponent.createCategory(toInput(dto));
    }

    @Override
    public Page<ProductDTO> findAllProducts(Pageable pageable, Map<String, String> filters) {
        hasAllowedSortProperties(pageable.getSort());
        checkSize(pageable);
        return productComponent.findAllProducts(pageable, filters)
                .map(this::toDTO);
    }

    private void checkSize(Pageable pageable) {
        if (pageable.getPageSize() > 100) {
            throw new ApplicationException(PAGE_SIZE_TOO_BIG);
        }
    }

    private void hasAllowedSortProperties(Sort sort) {
        if (sort.isSorted()) {
            Set<String> allowedProperties = Set.of("price", "stock", "id");
            Set<String> sortProperties = sort.stream()
                    .map(Sort.Order::getProperty)
                    .collect(toSet());
            if (!allowedProperties.containsAll(sortProperties)) {
                throw new ApplicationException(FORBIDDEN_SORT_PROPERTY);
            }
        }
    }

    private ProductDTO toDTO(ProductOutput pO) {
        CategoryDTO categoryDTO = toDTO(productComponent
                .findCategoryById(pO.getCategoryId()));
        UserInProductDTO userInProductDTO = toUserInProductDTO(userComponent.
                findByIdAndEnable(pO.getUserId(), true));

        return ProductDTO.builder()
                .id(pO.getId())
                .name(pO.getName())
                .price(pO.getPrice())
                .stock(pO.getStock())
                .description(pO.getDescription())
                .weightPounds(pO.getWeightPounds())
                .widthCM(pO.getWidthCM())
                .heightCM(pO.getHeightCM())
                .imageUrls(pO.getImageUrls())
                .category(categoryDTO)
                .user(userInProductDTO)
                .build();
    }


    private UserInProductDTO toUserInProductDTO(UserOutput user) {
        if (user == null) return UserInProductDTO.builder().build();
        return UserInProductDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }

    @Override
    public Page<ProductDTO> findMyProducts(Pageable pageable) {
        Long userId = securityComponent.getCurrentUserId();
        return productComponent.findProductByUserId(userId, pageable)
                .map(this::toDTO);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return toDTO(productComponent.findProductById(id));
    }

    @Override
    public void putProduct(Long id, CreateProductDTO createProductDTO) {
//        existProductById(id); // avoid creation complexity again
//        productComponent.updateProductById(id, toInput(createProductDTO));
    }

    @Override
    public void deleteProduct(Long productId) {
        Long userId = securityComponent.getCurrentUserId();
        productComponent.deleteProductByIdAndUserId(productId, userId);
    }


    private void existProductById(Long id) {
        if (!productComponent.existProductById(id)) {
            throw new ApplicationException(PRODUCT_NOT_FOUND_BY_ID);
        }
    }

    private CreateCategoryInput toInput(CreateCategoryDTO input) {
        CreateCategoryInput res = CreateCategoryInput.builder()
                .name(input.getName())
                .build();
        return res;
    }

    private CreateProductInput toInput(CreateProductDTO dto, Long userId) {
        return CreateProductInput.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .description(dto.getDescription())
                .weightPounds(dto.getWeightPounds())
                .widthCM(dto.getWidthCM())
                .heightCM(dto.getHeightCM())
                .lengthCM(dto.getLengthCM())
                .imageUrls(new HashSet<>(0)) // todo: put a def img url
                .categoryId(dto.getCategoryId())
                .userId(userId)
                .build();
    }
}
