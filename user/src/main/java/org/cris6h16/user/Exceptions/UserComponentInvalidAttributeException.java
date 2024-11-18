package org.cris6h16.user.Exceptions;

import lombok.Getter;

@Getter
public  class UserComponentInvalidAttributeException extends RuntimeException {
    private final UserErrorCode errorCode;

    public UserComponentInvalidAttributeException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
