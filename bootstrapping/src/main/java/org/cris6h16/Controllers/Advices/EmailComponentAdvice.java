package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.EmailComponentErrorMsgProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.email.Exceptions.EmailEmailSendingException;
import org.cris6h16.email.Exceptions.InvalidAttributeException.EmailInvalidAttributeException;
import org.cris6h16.email.Exceptions.InvalidAttributeException.EmailInvalidCodeLengthException;
import org.cris6h16.email.Exceptions.InvalidAttributeException.EmailInvalidEmailException;
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

    @ExceptionHandler(EmailInvalidAttributeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAttributeException(EmailInvalidAttributeException e) {
        log.debug("InvalidAttributeException", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(getMsg(e)));
    }

    private String getMsg(EmailInvalidAttributeException e) {
        if (e instanceof EmailInvalidEmailException) {
            return emailErrorMsgProperties.getEmailInvalid();
        }
        if (e instanceof EmailInvalidCodeLengthException) {
            return emailErrorMsgProperties.getInvalidCodeLength();
        }

        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }

    @ExceptionHandler(EmailEmailSendingException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendingException(EmailEmailSendingException e) {
        log.debug("EmailSendingException", e);
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(this.emailErrorMsgProperties.getEmailSending()));
    }

}
