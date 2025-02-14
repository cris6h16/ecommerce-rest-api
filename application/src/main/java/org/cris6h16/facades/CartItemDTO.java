package org.cris6h16.facades;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Builder
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Set<String> productImgUrl;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;
}
