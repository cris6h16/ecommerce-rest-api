package org.cris6h16.Exception;

import lombok.Getter;

@Getter
public class AddressComponentException extends RuntimeException {
    private final AddressErrorCode errorCode;

    public AddressComponentException(AddressErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }
}
