package org.cris6h16.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cris6h16.user.UserEntity;
import org.cris6h16.user.UserOutput;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOutput {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Integer approxWeightLb;
    private Integer approxWidthCm;
    private Integer approxHeightCm;
    private String imageUrl;
    private UserOutput user;
    private CategoryOutput category;
}
