package org.cris6h16.product.Exceptions;

import lombok.Getter;

@Getter
public class ProductComponentException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductComponentException(ProductErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }
}
