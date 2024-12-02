package org.cris6h16.cart;

import static org.cris6h16.cart.CartComponentErrorCode.CART_ITEM_ID_LESS_THAN_ONE;
import static org.cris6h16.cart.CartComponentErrorCode.INVALID_USER_ID;
import static org.cris6h16.cart.CartComponentErrorCode.NULL_CART_ITEM_ID;
import static org.cris6h16.cart.CartComponentErrorCode.NULL_PRODUCT_ID;
import static org.cris6h16.cart.CartComponentErrorCode.NULL_QUANTITY;
import static org.cris6h16.cart.CartComponentErrorCode.NULL_USER_ID;
import static org.cris6h16.cart.CartComponentErrorCode.PRODUCT_ID_LESS_THAN_ONE;
import static org.cris6h16.cart.CartComponentErrorCode.QUANTITY_LESS_THAN_ONE;
import static org.cris6h16.cart.CartComponentErrorCode.USER_ID_LESS_THAN_ONE;

public class CartValidator {
     void validate(CreateCartItemInput input) {
        validateProductId(input.getProductId());
        validateQuantity(input.getQuantity());
    }

     void validateQuantity(Integer quantity) {
        if (quantity == null) throwE(NULL_QUANTITY);
        if (quantity < 1) throwE(QUANTITY_LESS_THAN_ONE);
    }

     void validateProductId(Long productId) {
        if (productId == null) throwE(NULL_PRODUCT_ID);
        if (productId < 1) throwE(PRODUCT_ID_LESS_THAN_ONE);
    }

     void validateUserId(Long userId) {
        if (userId == null) throwE(NULL_USER_ID);
        if (userId < 1) throwE(USER_ID_LESS_THAN_ONE);
    }

     void throwE(CartComponentErrorCode errorCode) {
        throw new CartComponentException(errorCode);
    }

    void validateCartItemId(Long itemId) {
        if (itemId == null) throwE(NULL_CART_ITEM_ID);
        if (itemId < 1) throwE(CART_ITEM_ID_LESS_THAN_ONE);
    }
}
