package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.ApplicationComponentErrorMsgProperties;
import org.cris6h16.Controllers.Advices.Properties.CartErrorProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.cart.CartComponentException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
class CartComponentAdvice {


    private final ApplicationComponentErrorMsgProperties properties;
    private final CartErrorProperties cartErrorProperties;
    private final SystemErrorProperties systemErrorProperties;

    CartComponentAdvice(ApplicationComponentErrorMsgProperties properties, CartErrorProperties cartErrorProperties, SystemErrorProperties systemErrorProperties) {
        this.properties = properties;
        this.cartErrorProperties = cartErrorProperties;
        this.systemErrorProperties = systemErrorProperties;
    }

    @ExceptionHandler(CartComponentException.class)
    public ResponseEntity<ErrorResponse> handleCartComponentException(CartComponentException e) {
        return createResponseEntity(e);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(CartComponentException e) {
        HttpStatus status;
        String msg;

        switch (e.getErrorCode()) {
            case INVALID_QUANTITY:
                status = HttpStatus.BAD_REQUEST;
                msg = cartErrorProperties.getInvalidQuantity();
                break;
            case PRODUCT_ALREADY_IN_CART:
                status = HttpStatus.CONFLICT;
                msg = cartErrorProperties.getProductAlreadyInCart();
                break;
            case INVALID_DELTA:
                status = HttpStatus.BAD_REQUEST;
                msg = cartErrorProperties.getInvalidDelta();
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
