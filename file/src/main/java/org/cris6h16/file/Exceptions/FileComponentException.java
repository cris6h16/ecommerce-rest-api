package org.cris6h16.file.Exceptions;

import lombok.Getter;

@Getter
public class FileComponentException extends RuntimeException {
    FileErrorCode errorCode;

    public FileComponentException( FileErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
