package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.Controllers.Advices.Properties.UserComponentErrorMsgProperties;
import org.cris6h16.user.Exceptions.UserComponentAttributeAlreadyExistsException;
import org.cris6h16.user.Exceptions.UserComponentInvalidAttributeException;
import org.cris6h16.user.Exceptions.UserErrorCode;
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
public class UserComponentAdvice {


    private final SystemErrorProperties systemErrorProperties;

    private final UserComponentErrorMsgProperties userErrorMsgProperties;

    public UserComponentAdvice(SystemErrorProperties systemErrorProperties, UserComponentErrorMsgProperties userErrorMsgProperties) {
        this.systemErrorProperties = systemErrorProperties;
        this.userErrorMsgProperties = userErrorMsgProperties;
    }

    @ExceptionHandler(UserComponentAttributeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(UserComponentAttributeAlreadyExistsException e) {
        log.debug("AlreadyExistsException", e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(e.getErrorCode().name(), getMsg(e.getErrorCode())));
    }

    @ExceptionHandler(UserComponentInvalidAttributeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAttribute(UserComponentInvalidAttributeException e) {
        log.debug("UserInvalidAttributeException", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(e.getErrorCode().name(), getMsg(e.getErrorCode())));
    }

    private String getMsg(UserErrorCode e) {
        if (e.equals(UserErrorCode.EMAIL_ALREADY_EXISTS)) {
            return userErrorMsgProperties.getEmailAlreadyExists();
        }

        if (e.equals(UserErrorCode.EMAIL_NULL)) {
            return userErrorMsgProperties.getEmailNull();
        }

        if (e.equals(UserErrorCode.EMAIL_TOO_LONG)) {
            return userErrorMsgProperties.getEmailTooLong();
        }

        if (e.equals(UserErrorCode.EMAIL_REGEX_MISMATCH)) {
            return userErrorMsgProperties.getEmailInvalid();
        }

        if (e.equals(UserErrorCode.FIRSTNAME_NULL)) {
            return userErrorMsgProperties.getFirstnameNull();
        }

        if (e.equals(UserErrorCode.FIRSTNAME_TOO_LONG)) {
            return userErrorMsgProperties.getFirstnameTooLong();
        }

        if (e.equals(UserErrorCode.LASTNAME_NULL)) {
            return userErrorMsgProperties.getLastnameNull();
        }

        if (e.equals(UserErrorCode.LASTNAME_TOO_LONG)) {
            return userErrorMsgProperties.getLastnameTooLong();
        }

        if (e.equals(UserErrorCode.PASSWORD_NULL)) {
            return userErrorMsgProperties.getPasswordNull();
        }

        if (e.equals(UserErrorCode.PASSWORD_TOO_LONG)) {
            return userErrorMsgProperties.getPasswordTooLong();
        }

        if (e.equals(UserErrorCode.PASSWORD_LESS_THAN_8)) {
            return userErrorMsgProperties.getPasswordLessThan8();
        }

        if (e.equals(UserErrorCode.USER_ID_NULL)) {
            return userErrorMsgProperties.getUserIdNull();
        }

        if (e.equals(UserErrorCode.USER_ID_LESS_THAN_1)) {
            return userErrorMsgProperties.getUserIdLessThan1();
        }

        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }


}
