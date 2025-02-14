package org.cris6h16.cart;

import org.springframework.stereotype.Component;

import static org.cris6h16.cart.CartComponentErrorCode.CART_ITEM_ID_LESS_THAN_ONE;
import static org.cris6h16.cart.CartComponentErrorCode.INVALID_PRODUCT_ID;
import static org.cris6h16.cart.CartComponentErrorCode.INVALID_QUANTITY;
import static org.cris6h16.cart.CartComponentErrorCode.INVALID_USER_ID;
import static org.cris6h16.cart.CartComponentErrorCode.NULL_CART_ITEM_ID;

@Component
public class CartValidator {
    void validate(CreateCartItemInput input) {
        validateProductId(input.getProductId());
        validateQuantity(input.getQuantity());
    }

    void validateQuantity(Integer quantity) {
        if (quantity == null || quantity < 1) throwE(INVALID_QUANTITY);
    }

    void validateProductId(Long productId) {
        validateId(productId, INVALID_PRODUCT_ID);
    }

    void validateUserId(Long userId) {
        validateId(userId, INVALID_USER_ID);
    }

    void validateId(Long id, CartComponentErrorCode code){
        if (id == null || id <= 0) throwE(code);
    }

    void throwE(CartComponentErrorCode errorCode) {
        throw new CartComponentException(errorCode);
    }

    void validateCartItemId(Long itemId) {
        if (itemId == null) throwE(NULL_CART_ITEM_ID);
        if (itemId < 1) throwE(CART_ITEM_ID_LESS_THAN_ONE);
    }
}
