package org.cris6h16.cart;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
public class CartOutput {
    private Long id;
    private Set<CartItemOutput> items;
    private BigDecimal total;

    public CartOutput(Long id, Set<CartItemOutput> items) {
        this.id = id;
        this.items = items;
        this.total = items.stream()
                .map(CartItemOutput::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
