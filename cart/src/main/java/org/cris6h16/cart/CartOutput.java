package org.cris6h16.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class CartOutput {
    private Long id;
    private Set<CartItemOutput> items;
}
