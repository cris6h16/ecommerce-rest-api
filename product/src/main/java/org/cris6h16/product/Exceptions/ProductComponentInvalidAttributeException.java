package org.cris6h16.product.Exceptions;

import lombok.Getter;

@Getter
public class ProductComponentInvalidAttributeException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductComponentInvalidAttributeException(ProductErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
