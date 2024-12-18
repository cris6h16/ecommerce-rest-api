package org.cris6h16.facades;

import java.math.BigDecimal;
import java.util.Set;

public class CartDTO {
    private Long id;
    private Set<CartItemDTO> items;
    private BigDecimal total;

    public CartDTO(Long id, Set<CartItemDTO> items) {
        this.id = id;
        this.items = items;
        this.total = items.stream()
                .map(CartItemDTO::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
