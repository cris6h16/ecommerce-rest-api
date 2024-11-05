package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.EmailErrorMsgProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.email.Exceptions.InvalidAttributeException.InvalidAttributeException;
import org.cris6h16.email.Exceptions.InvalidAttributeException.InvalidCodeLengthException;
import org.cris6h16.email.Exceptions.InvalidAttributeException.InvalidEmailException;
import org.cris6h16.email.Exceptions.ValidVerificationCodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;

@RestControllerAdvice
@Slf4j
public class EmailControllerAdvice {

    private final SystemErrorProperties systemErrorProperties;

    private final EmailErrorMsgProperties emailErrorMsgProperties;

    public EmailControllerAdvice(SystemErrorProperties systemErrorProperties, EmailErrorMsgProperties emailErrorMsgProperties) {
        this.systemErrorProperties = systemErrorProperties;
        this.emailErrorMsgProperties = emailErrorMsgProperties;
    }

    @ExceptionHandler(InvalidAttributeException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(InvalidAttributeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(getMsg(e)));
    }

    private String getMsg(InvalidAttributeException e) {
        if (e instanceof InvalidEmailException) {
            return emailErrorMsgProperties.getEmailInvalid();
        }
        if (e instanceof InvalidCodeLengthException) {
            return emailErrorMsgProperties.getInvalidCodeLength();
        }

        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }

    @ExceptionHandler(ValidVerificationCodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(ValidVerificationCodeNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(emailErrorMsgProperties.getValidVerificationCodeNotFound()));
    }


}
