package org.cris6h16.facades;

public interface CartFacade {
    Long addItemToCart(CreateCartItemDTO dto);

    CartDTO getOrCreateMyCart();

    void updateCartItemQuantity(Long itemId, Integer quantity);

    void deleteCartItem(Long itemId);
}