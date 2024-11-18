package org.cris6h16.product.Exceptions;

import lombok.Getter;

@Getter
public class ProductComponentAlreadyExistsException extends RuntimeException {
    private final ProductErrorCode errorCode;

    public ProductComponentAlreadyExistsException(ProductErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
