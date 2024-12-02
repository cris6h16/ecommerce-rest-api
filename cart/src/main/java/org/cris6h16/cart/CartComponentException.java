package org.cris6h16.cart;

import lombok.Getter;

@Getter
public class CartComponentException extends RuntimeException {
    CartComponentErrorCode errorCode;
    public CartComponentException(CartComponentErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
