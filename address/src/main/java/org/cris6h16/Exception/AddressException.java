package org.cris6h16.Exception;

import lombok.Getter;

@Getter
public class AddressException  extends RuntimeException {
    private final AddressErrorCode errorCode;

    public AddressException(AddressErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }
}
