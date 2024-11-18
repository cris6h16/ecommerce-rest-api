package org.cris6h16.product.Exceptions;

import lombok.Getter;

@Getter
public class ProductComponentNotFoundException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductComponentNotFoundException(ProductErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
