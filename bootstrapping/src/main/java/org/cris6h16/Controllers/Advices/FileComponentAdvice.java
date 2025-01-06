package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.FileComponentErrorMsgProperty;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.file.Exceptions.FileComponentException;
import org.cris6h16.file.Exceptions.FileErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_DELETE_BY_URL_ERROR;
import static org.cris6h16.file.Exceptions.FileErrorCode.EMPTY_MULTIPART_FILE;
import static org.cris6h16.file.Exceptions.FileErrorCode.FILE_LIST_IS_EMPTY;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class FileComponentAdvice {

    private final FileComponentErrorMsgProperty msgs;
    private final SystemErrorProperties systemErrorProperties;

    public FileComponentAdvice(FileComponentErrorMsgProperty msgs, SystemErrorProperties systemErrorProperties) {
        this.msgs = msgs;
        this.systemErrorProperties = systemErrorProperties;
    }

    @ExceptionHandler(FileComponentException.class)
    public ResponseEntity<ErrorResponse> handleFileComponentException(FileComponentException e) {
        log.debug("FileComponentException", e);
        return createResponseEntity(e);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(FileComponentException e) {
        HttpStatus status;
        String msg;

        FileErrorCode ec = e.getErrorCode();
        if (ec.equals(FILE_DELETE_BY_URL_ERROR)) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            msg = msgs.getDeleteByUrlError();

        } else if (ec.equals(FileErrorCode.FILE_UPLOAD_ERROR) || ec.equals(FileErrorCode.FILE_DELETE_BY_URL_ALL_RETRIES_ERROR)) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            msg = msgs.getUploadError();

        } else if (ec.equals(FileErrorCode.FILE_UPLOAD_ALL_RETRIES_ERROR)) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            msg = msgs.getUploadAllRetriesError();

        } else if (ec.equals(EMPTY_MULTIPART_FILE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getEmptyMultipartFile();

        } else if (ec.equals(FILE_LIST_IS_EMPTY)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getFileListIsEmpty();

        }else if (ec.equals(FileErrorCode.FILE_SIZE_EXCEEDED)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getSizeExceeded();

        } else if (ec.equals(FileErrorCode.FILE_CONTENT_TYPE_IS_NOT_IMAGE) || ec.equals(FileErrorCode.FILE_CONTENT_TYPE_IS_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getContentTypeIsNotImage();

        } else {
            log.error("A custom exception should have custom response handling", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            msg = systemErrorProperties.getUnexpectedError();
        }

        return ResponseEntity
                .status(status)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(msg));
    }
}
