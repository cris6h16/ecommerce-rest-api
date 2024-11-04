package org.cris6h16;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Exceptions.AlreadyExistsException.AlreadyExistsException;
import org.cris6h16.Exceptions.AlreadyExistsException.EmailAlreadyExistsException;
import org.cris6h16.Exceptions.EmailNotVerifiedException;
import org.cris6h16.Exceptions.InvalidAttributeException.InvalidAttributeException;
import org.cris6h16.Exceptions.InvalidAttributeException.InvalidEmailException;
import org.cris6h16.Exceptions.InvalidAttributeException.InvalidFirstnameLengthException;
import org.cris6h16.Exceptions.InvalidAttributeException.InvalidLastnameLengthException;
import org.cris6h16.Exceptions.InvalidAttributeException.InvalidPasswordLengthException;
import org.cris6h16.Exceptions.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.cris6h16.UserConfig.jsonHeaderCons;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    private final UserErrorMsgProperties userErrorMsgProperties;

    public ControllerAdvice(UserErrorMsgProperties userErrorMsgProperties) {
        this.userErrorMsgProperties = userErrorMsgProperties;
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
        return userErrorMsgProperties.getUnexpectedError();
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
        return userErrorMsgProperties.getUnexpectedError();
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logIfRelevant(e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(userErrorMsgProperties.getUnexpectedError()));
    }
    private void logIfRelevant(Exception e) {
        if (e instanceof NoResourceFoundException) {
            log.info("Someone tried to access a non-existent resource");
        } else {
            log.error("Unexpected error", e);
        }
    }

    public static record ErrorResponse(String message) {
    }
}
