package org.cris6h16.cart;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CartItemOutput {
    private Long id;
    private Long productId;
    private Integer quantity;
}
