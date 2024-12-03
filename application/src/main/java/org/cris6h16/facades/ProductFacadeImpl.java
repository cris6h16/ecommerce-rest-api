package org.cris6h16.facades;

import lombok.extern.slf4j.Slf4j;
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
import org.cris6h16.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.cris6h16.facades.Exceptions.ApplicationErrorCode.PRODUCT_NOT_FOUND_BY_ID;
import static org.cris6h16.facades.FacadesCommon.isUserEnabled;

@Slf4j
@Component // todo: should be a custom annotation @Facade -> @Service
public class ProductFacadeImpl implements ProductFacade {
    private final ProductComponent productComponent;
    private final SecurityComponent securityComponent;
    private final FileComponent fileComponent;
    private final UserComponent userComponent;

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
        isUserEnabled(userId, );
        Long id = productComponent.createProduct(toInput(dto));
        Set<String> url = fileComponent.uploadImages(dto.getImages());
        productComponent.updateImagesById(id, url);
        return id;
    }

    @Override
    public Set<CategoryDTO> getCategories() {
        return productComponent.findAllCategories(Pageable.unpaged()).stream()
                .map(this::toProductDTO)
                .collect(toSet());
    }

    private CategoryDTO toProductDTO(CategoryOutput output) {
        log.debug("Converting CategoryOutput to CategoryDTO: {}", output);
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
        isUserEnabled(userId);
        return productComponent.createCategory(toInput(dto));
    }

    @Override
    public Page<ProductDTO> findAllProducts(Pageable pageable, Map<String, String> filters) {
        log.debug("Finding all products with pageable: {} and filters: {}", pageable, filters);
        return productComponent.findAllProducts(pageable, filters)
                .map(this::toProductDTO);
    }

    private ProductDTO toProductDTO(ProductOutput productOutput) {
        log.debug("Converting ProductOutput to ProductDTO: {}", productOutput);
        return ProductDTO.builder()
                .id(productOutput.getId())
                .name(productOutput.getName())
                .price(productOutput.getPrice())
                .stock(productOutput.getStock())
                .description(productOutput.getDescription())
                .approxWeightLb(productOutput.getApproxWeightLb())
                .approxWidthCm(productOutput.getApproxWidthCm())
                .approxHeightCm(productOutput.getApproxHeightCm())
                .imageUrls(productOutput.getImageUrls())
                .category(toProductDTO(productOutput.getCategory()))
                .user(toUserInProductDTO(productOutput.getUser()))
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
        isUserEnabled(userId);
        return productComponent.findProductByUserId(userId, pageable)
                .map(this::toProductDTO);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return toProductDTO(productComponent.findProductById(id));
    }

    @Override
    public void putProduct(Long id, CreateProductDTO createProductDTO) {
        existProductById(id); // avoid creation complexity again
        productComponent.updateProductById(id, toInput(createProductDTO));
    }

    @Override
    public void deleteProduct(Long productId) {
        Long userId = securityComponent.getCurrentUserId();
        isUserEnabled(userId);
        productComponent.deleteProductByIdAndUserId(productId, userId);
    }


    private void existProductById(Long id) {
        if (!productComponent.existProductById(id)) {
            throw new ApplicationException(PRODUCT_NOT_FOUND_BY_ID);
        }
    }

    private CreateCategoryInput toInput(CreateCategoryDTO input) {
        log.debug("Converting CreateCategoryDTO to CreateCategoryInput: {}", input);
        CreateCategoryInput res = CreateCategoryInput.builder()
                .name(input.getName())
                .build();
        log.debug("CreateCategoryInput created: {}", res);
        return res;
    }

    private CreateProductInput toInput(CreateProductDTO dto) {
        log.debug("Converting CreateProductDTO to CreateProductInput: {}", dto);
        Long userId = securityComponent.getCurrentUserId();
        isUserEnabled(userId);
        CreateProductInput res = CreateProductInput.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .description(dto.getDescription())
                .approxWeightLb(dto.getApproxWeightLb())
                .approxWidthCm(dto.getApproxWidthCm())
                .approxHeightCm(dto.getApproxHeightCm())
                .imageUrls(new HashSet<>(0)) // todo: put a def img url
                .categoryId(dto.getCategoryId())
                .userId(userId)
                .build();
        log.debug("CreateProductInput created: {}", res);
        return res;
    }
}
