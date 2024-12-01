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
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_REGEX_MISMATCH;
import static org.cris6h16.user.Exceptions.UserErrorCode.EMAIL_TOO_LONG;
import static org.cris6h16.user.Exceptions.UserErrorCode.FIRSTNAME_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.FIRSTNAME_TOO_LONG;
import static org.cris6h16.user.Exceptions.UserErrorCode.LASTNAME_NULL;
import static org.cris6h16.user.Exceptions.UserErrorCode.PASSWORD_TOO_LONG;

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
        if (code.equals(FIRSTNAME_TOO_LONG)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getFirstnameTooLong();

        } else if (code.equals(FIRSTNAME_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getFirstnameNull();

        } else if (code.equals(LASTNAME_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getLastnameNull();

        } else if (code.equals(UserErrorCode.LASTNAME_TOO_LONG)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getLastnameTooLong();

        } else if (code.equals(UserErrorCode.PASSWORD_NULL) || code.equals(UserErrorCode.PASSWORD_LESS_THAN_8)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getPasswordLessThan8();

        } else if (code.equals(PASSWORD_TOO_LONG)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getPasswordTooLong();

        } else if (code.equals(EMAIL_NULL) || code.equals(EMAIL_TOO_LONG) || code.equals(EMAIL_REGEX_MISMATCH)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getEmailInvalid();

        } else if (code.equals(UserErrorCode.USER_ID_NULL) || code.equals(UserErrorCode.USER_ID_LESS_THAN_1)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getUserIdInvalid();

        } else if (code.equals(UserErrorCode.EMAIL_ALREADY_EXISTS)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getEmailAlreadyExists();

        } else {
            log.error("A custom exception should have custom response handling", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            msg = systemErrorProperties.getUnexpectedError();

        }

        return ResponseEntity
                .status(status)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(
                        code.name(),
                        msg
                ));
    }

}
