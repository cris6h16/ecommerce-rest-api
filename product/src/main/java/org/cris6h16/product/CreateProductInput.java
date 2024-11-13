package org.cris6h16.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
public class CreateProductInput implements Prepareable{
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Integer approxWeightLb;
    private Integer approxWidthCm;
    private Integer approxHeightCm;
    private String imageUrl;
    private Long categoryId;
    private Long userId;
    private Long brandId; // todo: SN default

    @Override
    public void trim() {
        name = name.trim();
        description = description.trim();
        imageUrl = imageUrl.trim();
    }

    @Override
    public void nullToEmpty() {
        name = name == null ? "" : name;
        price = price == null ? BigDecimal.ZERO : price;
        stock = stock == null ? 0 : stock;
        description = description == null ? "" : description;
        approxWeightLb = approxWeightLb == null ? 0 : approxWeightLb;
        approxWidthCm = approxWidthCm == null ? 0 : approxWidthCm;
        approxHeightCm = approxHeightCm == null ? 0 : approxHeightCm;
        imageUrl = imageUrl == null ? "" : imageUrl;
    }
}
