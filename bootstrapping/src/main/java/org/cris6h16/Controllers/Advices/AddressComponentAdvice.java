package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.AddressErrorProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.Exception.AddressComponentException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AddressComponentAdvice {


    private final AddressErrorProperties cartErrorProperties;
    private final SystemErrorProperties systemErrorProperties;

    AddressComponentAdvice( AddressErrorProperties cartErrorProperties, SystemErrorProperties systemErrorProperties) {
        this.cartErrorProperties = cartErrorProperties;
        this.systemErrorProperties = systemErrorProperties;
    }

    @ExceptionHandler(AddressComponentException.class)
    public ResponseEntity<ErrorResponse> handleAddressComponentException(AddressComponentException e) {
        return createResponseEntity(e);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(AddressComponentException e) {
        HttpStatus status;
        String msg;

        switch (e.getErrorCode()) {
            case INVALID_STREET:
                status = HttpStatus.BAD_REQUEST;
                msg = cartErrorProperties.getInvalidStreet();
                break;
            case INVALID_REFERENCE:
                status = HttpStatus.BAD_REQUEST;
                msg = cartErrorProperties.getInvalidReference();
                break;
            case INVALID_STATE:
                status = HttpStatus.BAD_REQUEST;
                msg = cartErrorProperties.getInvalidState();
                break;
            case INVALID_MOBILE_NUMBER:
                status = HttpStatus.BAD_REQUEST;
                msg = cartErrorProperties.getInvalidMobileNumber();
                break;
            case INVALID_CITY:
                status = HttpStatus.BAD_REQUEST;
                msg = cartErrorProperties.getInvalidCity();
                break;
              default:
                log.error("A custom exception should have custom response handling", e);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                msg = systemErrorProperties.getUnexpectedError();
                break;
        }

        return ResponseEntity.status(status).body(new ErrorResponse(msg));
    }
}
