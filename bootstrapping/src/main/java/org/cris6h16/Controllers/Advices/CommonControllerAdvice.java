package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class CommonControllerAdvice {

    private final SystemErrorProperties systemErrorProperties;

    public CommonControllerAdvice(SystemErrorProperties systemErrorProperties) {
        this.systemErrorProperties = systemErrorProperties;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logIfRelevant(e);
        log.debug("Exception", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(systemErrorProperties.getUnexpectedError()));
    }

    private void logIfRelevant(Exception e) {
        if (e instanceof NoResourceFoundException) return;
        log.error("Unexpected error", e);
    }

}
