package org.cris6h16.cart;

public interface CartComponent {
    Long addItemToCart(CreateCartItemInput input, Long id);
}
