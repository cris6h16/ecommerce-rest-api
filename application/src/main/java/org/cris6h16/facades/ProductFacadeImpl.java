package org.cris6h16.facades;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.CreateProductInput;
import org.cris6h16.product.ProductComponent;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class ProductFacadeImpl implements ProductFacade {
    private final ProductComponent productComponent;

    public ProductFacadeImpl(ProductComponent productComponent) {
        this.productComponent = productComponent;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public Long save(CreateProductDTO dto) {
        return productComponent.save(toInput(dto));
    }

    private CreateProductInput toInput(CreateProductDTO dto) {
        log.info("Converting CreateProductDTO to CreateProductInput: {}", dto);
        CreateProductInput res = CreateProductInput.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .description(dto.getDescription())
                .approxWeightLb(dto.getApproxWeightLb())
                .approxWidthCm(dto.getApproxWidthCm())
                .approxHeightCm(dto.getApproxHeightCm())
                .imageUrl(dto.getImageUrl())
                .build();
        log.info("CreateProductInput created: {}", res);
        return res;
    }
}
