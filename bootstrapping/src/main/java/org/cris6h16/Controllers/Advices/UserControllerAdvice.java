package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.Controllers.Advices.Properties.UserErrorMsgProperties;
import org.cris6h16.user.Exceptions.AlreadyExistsException.AlreadyExistsException;
import org.cris6h16.user.Exceptions.AlreadyExistsException.EmailAlreadyExistsException;
import org.cris6h16.user.Exceptions.EmailNotVerifiedException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.InvalidAttributeException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.InvalidEmailException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.InvalidFirstnameLengthException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.InvalidLastnameLengthException;
import org.cris6h16.user.Exceptions.InvalidAttributeException.InvalidPasswordLengthException;
import org.cris6h16.user.Exceptions.InvalidCredentialsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class UserControllerAdvice {

    private final UserErrorMsgProperties userErrorMsgProperties;
    private final SystemErrorProperties systemErrorProperties;

    public UserControllerAdvice(UserErrorMsgProperties userErrorMsgProperties, SystemErrorProperties systemErrorProperties) {
        this.userErrorMsgProperties = userErrorMsgProperties;
        this.systemErrorProperties = systemErrorProperties;
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(AlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(getMsg(e)));
    }

    private String getMsg(AlreadyExistsException e) {
        if (e instanceof EmailAlreadyExistsException) {
            return userErrorMsgProperties.getEmailAlreadyExists();
        }
        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }


    @ExceptionHandler(InvalidAttributeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAttribute(InvalidAttributeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(getMsg(e)));
    }

    private String getMsg(InvalidAttributeException e) {
        if (e instanceof InvalidEmailException) {
            return userErrorMsgProperties.getEmailInvalid();
        }
        if (e instanceof InvalidFirstnameLengthException) {
            return userErrorMsgProperties.getFirstNameLength();
        }
        if (e instanceof InvalidLastnameLengthException) {
            return userErrorMsgProperties.getLastNameLength();
        }
        if (e instanceof InvalidPasswordLengthException) {
            return userErrorMsgProperties.getPasswordLength();
        }

        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotVerified(EmailNotVerifiedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(userErrorMsgProperties.getEmailNotVerified()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(userErrorMsgProperties.getInvalidCredentials()));
    }
}
