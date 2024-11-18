package org.cris6h16.facades;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.file.FileComponent;
import org.cris6h16.product.CategoryOutput;
import org.cris6h16.product.CreateCategoryInput;
import org.cris6h16.product.CreateProductInput;
import org.cris6h16.product.ProductComponent;
import org.cris6h16.security.SecurityComponent;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Component // todo: should be a custom annotation @Facade -> @Service
public class ProductFacadeImpl implements ProductFacade {
    private final ProductComponent productComponent;
    private final SecurityComponent securityComponent;
    private final FileComponent fileComponent;

    public ProductFacadeImpl(ProductComponent productComponent, SecurityComponent securityComponent, FileComponent fileComponent) {
        this.productComponent = productComponent;
        this.securityComponent = securityComponent;
        this.fileComponent = fileComponent;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public Long createProduct(CreateProductDTO dto) {
        Long id = productComponent.createProduct(toInput(dto));
        String url = fileComponent.upload(IfImgIsNotEmpty(dto.getImage()));
        productComponent.updateImageUrlById(id, url);
        return id;
    }

    @Override
    public Set<CategoryDTO> getCategories() {
        return productComponent.findAllCategories(Pageable.unpaged()).stream()
                .map(this::toDTO)
                .collect(toSet());
    }

    private CategoryDTO toDTO(CategoryOutput output) {
        return CategoryDTO.builder()
                .id(output.getId())
                .name(output.getName())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public Long createCategory(CreateCategoryDTO dto) {
        return productComponent.createCategory(toInput(dto));
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
        MultipartFile imgF = dto.getImage();
        CreateProductInput res = CreateProductInput.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .description(dto.getDescription())
                .approxWeightLb(dto.getApproxWeightLb())
                .approxWidthCm(dto.getApproxWidthCm())
                .approxHeightCm(dto.getApproxHeightCm())
                .imageUrl("fake-url://etc") // todo: put a def img url
                .categoryId(dto.getCategoryId())
                .userId(securityComponent.getCurrentUserId())
                .build();
        log.debug("CreateProductInput created: {}", res);
        return res;
    }

    private MultipartFile IfImgIsNotEmpty(MultipartFile imgF) {
        if (imgF.isEmpty()) {
            throw new ApplicationImgMultipartFileIsEmptyException();
        }

        return imgF;
    }
}
