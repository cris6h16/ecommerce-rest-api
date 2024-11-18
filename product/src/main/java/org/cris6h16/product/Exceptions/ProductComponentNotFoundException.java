package org.cris6h16.product.Exceptions;

import lombok.Getter;

@Getter
public class ProductComponentNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProductComponentNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
