package org.cris6h16.user.Exceptions;

import lombok.Getter;

@Getter
public  class UserComponentAttributeAlreadyExistsException extends RuntimeException {
    private final UserErrorCode errorCode;

    public UserComponentAttributeAlreadyExistsException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
