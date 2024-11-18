package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.EmailComponentErrorMsgProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.email.Exceptions.EmailComponentEmailSendingException;
import org.cris6h16.email.Exceptions.EmailErrorCode;
import org.cris6h16.email.Exceptions.EmailComponentInvalidAttributeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class EmailComponentAdvice {

    private final EmailComponentErrorMsgProperties emailErrorMsgProperties;
    private final SystemErrorProperties systemErrorProperties;

    public EmailComponentAdvice(EmailComponentErrorMsgProperties emailErrorMsgProperties, SystemErrorProperties systemErrorProperties) {
        this.emailErrorMsgProperties = emailErrorMsgProperties;
        this.systemErrorProperties = systemErrorProperties;
    }


    @ExceptionHandler(EmailComponentEmailSendingException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendingException(EmailComponentEmailSendingException e) {
        log.debug("EmailSendingException", e);
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse("EMAIL_SENDING_FAIL", this.emailErrorMsgProperties.getEmailSending()));
    }

    @ExceptionHandler(EmailComponentInvalidAttributeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAttributeException(EmailComponentInvalidAttributeException e) {
        log.debug("InvalidAttributeException", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(e.getErrorCode().name(), getMsg(e)));
    }

    private String getMsg(EmailComponentInvalidAttributeException e) {
        if (e.getErrorCode().equals(EmailErrorCode.EMAIL_REGEX_MISMATCH)) {
            return emailErrorMsgProperties.getEmailInvalid();
        }
        if (e.getErrorCode().equals(EmailErrorCode.CODE_INVALID_LENGTH)) {
            return emailErrorMsgProperties.getInvalidCodeLength();
        }

        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }


}
