package org.cris6h16.cart;

public interface CartComponent {
    Long addItemToCart(CreateCartItemInput input, Long userId);

    CartOutput getOrCreateCartByUserId(Long userId);


    void deleteCartItemById(Long itemId);

    void updateCartItemQuantityById(Integer delta, Long itemId, int stock);

    boolean isOwnerOfCartItem(Long userId, Long itemId);

    Long findProductIdByItemId(Long itemId);
}
