package org.cris6h16.product.Exceptions;

import lombok.Getter;

@Getter
public class ProductComponentException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductComponentException(ProductErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
