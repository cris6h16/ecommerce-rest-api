package org.cris6h16.email.Exceptions;

import lombok.Getter;

@Getter
public class EmailComponentInvalidAttributeException extends RuntimeException {
    private final EmailErrorCode errorCode;

    public EmailComponentInvalidAttributeException(EmailErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}