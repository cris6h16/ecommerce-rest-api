package org.cris6h16.email.Exceptions;

import lombok.Getter;

@Getter
public class EmailComponentException extends RuntimeException {
    private final EmailErrorCode errorCode;

    public EmailComponentException(EmailErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }
}