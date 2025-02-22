package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.ApplicationComponentErrorMsgProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.facades.Exceptions.ApplicationErrorCode;
import org.cris6h16.facades.Exceptions.ApplicationException;
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
    private final SystemErrorProperties systemErrorProperties;

    public ApplicationComponentAdvice(ApplicationComponentErrorMsgProperties properties, SystemErrorProperties systemErrorProperties) {
        this.properties = properties;
        this.systemErrorProperties = systemErrorProperties;
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        log.debug("ApplicationException", e);
        return createResponseEntity(e);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(ApplicationException e) {
        HttpStatus status;
        String msg;

        ApplicationErrorCode errorCode = e.getErrorCode();
        switch (errorCode) {
            case INVALID_CREDENTIALS:
                status = HttpStatus.FORBIDDEN;
                msg = properties.getInvalidCredentials();
                break;

            case EMAIL_NOT_VERIFIED:
                status = HttpStatus.FORBIDDEN;
                msg = properties.getEmailNotVerified();
                break;

            case VALID_VERIFICATION_CODE_NOT_FOUND:
                status = HttpStatus.FORBIDDEN;
                msg = properties.getValidVerificationCodeNotFound();
                break;

            case USER_NOT_FOUND_BY_ID:
                status = HttpStatus.NOT_FOUND;
                msg = properties.getEnabledUserNotFound();
                break;

            case ENABLED_USER_NOT_FOUND:
                status = HttpStatus.NOT_FOUND;
                msg = properties.getEnabledUserNotFound();
                break;

            case PRODUCT_NOT_FOUND_BY_ID:
                status = HttpStatus.NOT_FOUND;
                msg = properties.getProductNotFoundById();
                break;

            case UNSUPPORTED_ACTION_TYPE:
                status = HttpStatus.BAD_REQUEST;
                msg = properties.getUnsupportedActionType();
                break;

            case FORBIDDEN_SORT_PROPERTY:
                status = HttpStatus.FORBIDDEN;
                msg = properties.getForbiddenSortProperty();
                break;

            case PAGE_SIZE_TOO_BIG:
                status = HttpStatus.FORBIDDEN;
                msg = properties.getPageSizeTooBig();
                break;

            case INSUFFICIENT_STOCK:
                status = HttpStatus.CONFLICT;
                msg = properties.getInsufficientStock();
                break;
            case CART_ITEM_NOT_FOUND:
                status = HttpStatus.NOT_FOUND;
                msg = properties.getCartItemNotFound();
                break;
            case ADDRESS_NOT_FOUND:
                status = HttpStatus.NOT_FOUND;
                msg = properties.getAddressNotFound();
                break;

            default:
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
