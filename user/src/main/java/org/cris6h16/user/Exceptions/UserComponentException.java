package org.cris6h16.user.Exceptions;

import lombok.Getter;

@Getter
public  class UserComponentException extends RuntimeException {
    private final UserErrorCode errorCode;

    public UserComponentException(UserErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }
}
