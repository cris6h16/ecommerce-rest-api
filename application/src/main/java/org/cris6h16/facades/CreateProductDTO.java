package org.cris6h16.facades;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
public class CreateProductDTO {
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Integer approxWeightLb;
    private Integer approxWidthCm;
    private Integer approxHeightCm;
    private String imageUrl;
    private Long brandId;
    private Long categoryId;
}
