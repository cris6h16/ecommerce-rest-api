package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.FileComponentErrorMsgProperty;
import org.cris6h16.file.Exceptions.FileComponentFileUploadException;
import org.cris6h16.file.FileComponent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class FileComponentAdvice {

    private final FileComponentErrorMsgProperty msgs;

    public FileComponentAdvice(FileComponentErrorMsgProperty msgs) {
        this.msgs = msgs;
    }

    @ExceptionHandler(FileComponentFileUploadException.class)
    public ResponseEntity<ErrorResponse> handleFileComponentFileUploadException(FileComponentFileUploadException e) {
        log.debug("FileComponentFileUploadException: {}", e.getMessage());
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse("FILE_UPLOAD_FAIL", msgs.getFileUploadFail()));
    }
}
