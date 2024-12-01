package org.cris6h16.facades;

public interface CartFacade {
    Long addItemToCart(CreateCartItemDTO dto);

    CartDTO getMyCart();

    void updateCartItem(Long itemId, CreateCartItemDTO dto);

    void deleteCartItem(Long itemId);
}
