package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.Controllers.Advices.Properties.UserComponentErrorMsgProperties;
import org.cris6h16.user.Exceptions.AlreadyExistsException.UserAttributeAlreadyExistsException;
import org.cris6h16.user.Exceptions.AlreadyExistsException.UserEmailAlreadyExistsException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidAttributeException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidEmailException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidFirstnameLengthException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidLastnameLengthException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.UserInvalidPasswordLengthException;
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

    @ExceptionHandler(UserAttributeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(UserAttributeAlreadyExistsException e) {
        log.debug("AlreadyExistsException", e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(getMsg(e)));
    }

    private String getMsg(UserAttributeAlreadyExistsException e) {
        if (e instanceof UserEmailAlreadyExistsException) {
            return userErrorMsgProperties.getEmailAlreadyExists();
        }
        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }


    @ExceptionHandler(UserInvalidAttributeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAttribute(UserInvalidAttributeException e) {
        log.debug("UserInvalidAttributeException", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(getMsg(e)));
    }

    private String getMsg(UserInvalidAttributeException e) {
        if (e instanceof UserInvalidEmailException) {
            return userErrorMsgProperties.getEmailInvalid();
        }
        if (e instanceof UserInvalidFirstnameLengthException) {
            return userErrorMsgProperties.getFirstNameLength();
        }
        if (e instanceof UserInvalidLastnameLengthException) {
            return userErrorMsgProperties.getLastNameLength();
        }
        if (e instanceof UserInvalidPasswordLengthException) {
            return userErrorMsgProperties.getPasswordLength();
        }

        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }


}
