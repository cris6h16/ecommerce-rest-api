package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.EmailComponentErrorMsgProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.email.Exceptions.EmailComponentException;
import org.cris6h16.email.Exceptions.EmailErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.cris6h16.email.Exceptions.EmailErrorCode.ACTION_TYPE_BLANK;
import static org.cris6h16.email.Exceptions.EmailErrorCode.ACTION_TYPE_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.ACTION_TYPE_TOO_LONG;
import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_INVALID_LENGTH;
import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_IS_BLANK;
import static org.cris6h16.email.Exceptions.EmailErrorCode.CODE_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_NULL;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_REGEX_MISMATCH;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_SENDING_MAX_RETRIES_ERROR;
import static org.cris6h16.email.Exceptions.EmailErrorCode.EMAIL_TOO_LONG;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class EmailComponentAdvice {

    private final EmailComponentErrorMsgProperties props;
    private final SystemErrorProperties systemErrorProperties;

    public EmailComponentAdvice(EmailComponentErrorMsgProperties emailErrorMsgProperties, SystemErrorProperties systemErrorProperties) {
        this.props = emailErrorMsgProperties;
        this.systemErrorProperties = systemErrorProperties;
    }


    @ExceptionHandler(EmailComponentException.class)
    public ResponseEntity<ErrorResponse> handleEmailComponentException(EmailComponentException e) {
        log.debug("EmailComponentException", e);
        return createResponseEntity(e);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(EmailComponentException e) {
        HttpStatus status;
        String msg;

        /*
            EMAIL_TOO_LONG,
            EMAIL_REGEX_MISMATCH,
            CODE_NULL,
            EMAIL_NULL,
            CODE_IS_BLANK,
            CODE_INVALID_LENGTH,
            EMAIL_SENDING_MAX_RETRIES_ERROR;
            ACTION_TYPE_BLANK,
            ACTION_TYPE_NULL,
            ACTION_TYPE_TOO_LONG,
         */
        EmailErrorCode code = e.getErrorCode();
        if (code.equals(EMAIL_TOO_LONG) || code.equals(EMAIL_NULL) || code.equals(EMAIL_REGEX_MISMATCH)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getEmailInvalid();

        } else if (code.equals(EMAIL_SENDING_MAX_RETRIES_ERROR)) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            msg = props.getEmailSending();

        } else if (code.equals(CODE_NULL) || code.equals(CODE_IS_BLANK) || code.equals(CODE_INVALID_LENGTH)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getInvalidCodeLength();

        } else if (code.equals(ACTION_TYPE_BLANK) || code.equals(ACTION_TYPE_NULL) || code.equals(ACTION_TYPE_TOO_LONG)) {
            status = HttpStatus.BAD_REQUEST;
            msg = props.getInvalidActionTypeLength();

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
