package org.cris6h16.product.Exceptions;

import lombok.Getter;

@Getter
public class ProductAlreadyExistsException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProductAlreadyExistsException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
