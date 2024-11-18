package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.ApplicationComponentErrorMsgProperties;
import org.cris6h16.facades.ApplicationEmailNotVerifiedException;
import org.cris6h16.facades.ApplicationEnabledUserNotFoundException;
import org.cris6h16.facades.ApplicationInvalidCredentialsException;
import org.cris6h16.facades.ApplicationValidVerificationCodeNotFoundException;
import org.cris6h16.facades.ApplicationImgMultipartFileIsEmptyException;
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
public class ApplicationComponentAdvice {

    private final ApplicationComponentErrorMsgProperties properties;

    public ApplicationComponentAdvice(ApplicationComponentErrorMsgProperties properties) {
        this.properties = properties;
    }

    @ExceptionHandler(ApplicationValidVerificationCodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApplicationValidVerificationCodeNotFoundException(ApplicationValidVerificationCodeNotFoundException e) {
        log.debug("ApplicationValidVerificationCodeNotFoundException", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(
                        "VALID_VERIF_CODE_NOT_FOUND",
                        properties.getValidVerificationCodeNotFound())
                );
    }


    @ExceptionHandler(ApplicationEmailNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleApplicationEmailNotVerifiedException(ApplicationEmailNotVerifiedException e) {
        log.debug("ApplicationEmailNotVerifiedException", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(
                        "EMAIL_NOT_VERIFIED",
                        properties.getEmailNotVerified()
                ));
    }


    @ExceptionHandler(ApplicationInvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleApplicationInvalidCredentialsException(ApplicationInvalidCredentialsException e) {
        log.debug("ApplicationInvalidCredentialsException", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(
                        "INVALID_CREDENTIALS",
                        properties.getInvalidCredentials()
                ));
    }


    @ExceptionHandler(ApplicationImgMultipartFileIsEmptyException.class)
    public ResponseEntity<ErrorResponse> handleApplicationImgMultipartFileIsEmptyException(ApplicationImgMultipartFileIsEmptyException e) {
        log.debug("ApplicationImgMultipartFileIsEmptyException", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(
                        "IMG_FILE_IS_EMPTY",
                        properties.getImgMultipartFileIsEmpty()
                ));
    }


    @ExceptionHandler(ApplicationEnabledUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApplicationEnabledUserNotFoundException(ApplicationEnabledUserNotFoundException e) {
        log.debug("ApplicationEnabledUserNotFoundException", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(
                        "ENABLED_USER_NOT_FOUND",
                        properties.getEnabledUserNotFound()
                ));
    }

}
