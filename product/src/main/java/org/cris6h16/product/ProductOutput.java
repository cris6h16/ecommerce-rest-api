package org.cris6h16.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductOutput {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private BigDecimal weightPounds;
    private Integer widthCM;
    private Integer heightCM;
    private Integer lengthCM;
    private Set<String> imageUrls;
    private Long userId;
    private Long categoryId;
}
