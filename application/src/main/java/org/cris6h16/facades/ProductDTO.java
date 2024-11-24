package org.cris6h16.facades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Integer approxWeightLb;
    private Integer approxWidthCm;
    private Integer approxHeightCm;
    private String imageUrl;
    private UserInProductDTO user;
    private CategoryDTO category;
}
