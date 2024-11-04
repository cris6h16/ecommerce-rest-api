package org.cris6h16.email;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.email.Exceptions.InvalidAttributeException.InvalidAttributeException;
import org.cris6h16.email.Exceptions.InvalidAttributeException.InvalidCodeLengthException;
import org.cris6h16.email.Exceptions.InvalidAttributeException.InvalidEmailException;
import org.cris6h16.email.Exceptions.ValidVerificationCodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.cris6h16.email.EmailCommons.jsonHeaderCons;

@RestControllerAdvice
@Slf4j
public class EmailControllerAdvice {

    private final EmailErrorMsgProperties emailErrorMsgProperties;

    public EmailControllerAdvice(EmailErrorMsgProperties emailErrorMsgProperties) {
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
        return emailErrorMsgProperties.getUnexpectedError();
    }

    @ExceptionHandler(ValidVerificationCodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(ValidVerificationCodeNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(emailErrorMsgProperties.getValidVerificationCodeNotFound()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logIfRelevant(e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(emailErrorMsgProperties.getUnexpectedError()));
    }

    private void logIfRelevant(Exception e) {
        if (e instanceof NoResourceFoundException) {
            log.debug("Someone tried to access a non-existent resource");
        } else {
            log.error("Unexpected error", e);
        }
    }


    public static record ErrorResponse(String message) {
    }
}
