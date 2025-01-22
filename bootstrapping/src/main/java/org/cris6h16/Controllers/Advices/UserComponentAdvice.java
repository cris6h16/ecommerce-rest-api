package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.Controllers.Advices.Properties.UserComponentErrorMsgProperties;
import org.cris6h16.user.Exceptions.UserComponentException;
import org.cris6h16.user.Exceptions.UserErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_REGEX_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_TOO_LONG;
import static org.cris6h16.user.Exceptions.UserErrorCode.FIRSTNAME_LENGTH_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.LASTNAME_LENGTH_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.PASSWORD_LENGTH_MISMATCH;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class UserComponentAdvice {


    private final SystemErrorProperties systemErrorProperties;

    private final UserComponentErrorMsgProperties props;

    public UserComponentAdvice(SystemErrorProperties systemErrorProperties, UserComponentErrorMsgProperties userErrorMsgProperties) {
        this.systemErrorProperties = systemErrorProperties;
        this.props = userErrorMsgProperties;
    }

    @ExceptionHandler(UserComponentException.class)
    public ResponseEntity<ErrorResponse> handleUserComponentException(UserComponentException e) {
        log.debug("UserComponentException", e);
        return createResponseEntity(e);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(UserComponentException e) {
        HttpStatus status;
        String msg;

        UserErrorCode code = e.getErrorCode();

        if (code.equals(EMAIL_TOO_LONG)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getEmailTooLong();

        } else if (code.equals(EMAIL_REGEX_MISMATCH)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getEmailRegexMismatch();

        } else if (code.equals(UserErrorCode.USER_ID_LESS_THAN_1)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getUserIdLessThan1();

        } else if (code.equals(UserErrorCode.USER_NOT_FOUND)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getUserNotFound();

        } else if (code.equals(UserErrorCode.INVALID_BALANCE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getInvalidBalance();

        } else if (code.equals(UserErrorCode.EMAIL_ALREADY_EXISTS)) {
            status = HttpStatus.CONFLICT;
            msg = props.getEmailAlreadyExists();

        } else if (code.equals(FIRSTNAME_LENGTH_MISMATCH)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getFirstnameLengthMismatch();

        } else if (code.equals(LASTNAME_LENGTH_MISMATCH)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getLastnameLengthMismatch();

        } else if (code.equals(PASSWORD_LENGTH_MISMATCH)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getPasswordLengthMismatch();

        } else {
            log.error("A custom exception should have custom response handling", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            msg = systemErrorProperties.getUnexpectedError();

        }

        return ResponseEntity
                .status(status)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(msg));
    }

}
