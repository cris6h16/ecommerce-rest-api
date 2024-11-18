package org.cris6h16.product.Exceptions;

import lombok.Getter;

@Getter
public class ProductInvalidAttributeException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProductInvalidAttributeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
