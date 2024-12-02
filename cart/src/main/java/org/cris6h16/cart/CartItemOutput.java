package org.cris6h16.cart;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CartItemOutput {
    private Long id;
    private Long productId;
    private String productName;
    private String productImgUrl;
    private Integer quantity;
    private BigDecimal price;

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
