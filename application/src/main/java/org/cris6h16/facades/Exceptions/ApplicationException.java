package org.cris6h16.facades.Exceptions;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApplicationErrorCode errorCode;

    public ApplicationException(ApplicationErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
