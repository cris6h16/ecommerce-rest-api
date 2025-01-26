package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.ProductComponentErrorMsgProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.product.Exceptions.ProductComponentException;
import org.cris6h16.product.Exceptions.ProductErrorCode;
import org.cris6h16.product.ProductEntity;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.cris6h16.Controllers.HTTPCommons.jsonHeaderCons;
import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_NOT_FOUND_BY_ID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.IMAGE_URL_TOO_LONG;
import static org.cris6h16.product.Exceptions.ProductErrorCode.INVALID_CATEGORY_ID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.INVALID_PRICE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.INVALID_STOCK;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRICE_FILTER_ERROR_PARSING_STR_TO_BIGDECIMAL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_DESCRIPTION_LENGTH_MISMATCH;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NAME_LENGTH_MISMATCH;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NOT_FOUND_BY_ID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.STOCK_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.UNIQUE_USER_ID_PRODUCT_NAME;
import static org.cris6h16.product.Exceptions.ProductErrorCode.UNSUPPORTED_FILTER_ATTRIBUTE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.USER_NOT_FOUND_BY_ID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ProductComponentAdvice {
    private final ProductComponentErrorMsgProperties msgs;
    private final SystemErrorProperties systemErrorProperties;

    public ProductComponentAdvice(ProductComponentErrorMsgProperties productErrorMsgProperties, SystemErrorProperties systemErrorProperties) {
        this.msgs = productErrorMsgProperties;
        this.systemErrorProperties = systemErrorProperties;
    }

    @ExceptionHandler(ProductComponentException.class)
    public ResponseEntity<ErrorResponse> handleProductComponentException(ProductComponentException e) {
        log.debug("ProductComponentException", e);
        return createResponseEntity(e);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(ProductComponentException ex) {
        HttpStatus status;
        String msg;

        ProductErrorCode e = ex.getErrorCode();

          if (e.equals(PRODUCT_NAME_LENGTH_MISMATCH)) {
            status = BAD_REQUEST;
            msg = msgs.getProductNameLengthMismatch();

        }else if (e.equals(PRODUCT_DESCRIPTION_LENGTH_MISMATCH)) {
            status = BAD_REQUEST;
            msg = msgs.getProductDescriptionLengthMismatch();

        } else if (e.equals(UNIQUE_USER_ID_PRODUCT_NAME)) {
            status = CONFLICT;
            msg = msgs.getUniqueUserIdProductName();

        }else if (e.equals(INVALID_STOCK)) {
            status = BAD_REQUEST;
            msg = msgs.getInvalidStock();

        }else if (e.equals(INVALID_PRICE)) {
            status = BAD_REQUEST;
            msg = msgs.getInvalidPrice();

        }   else if (e.equals(IMAGE_URL_TOO_LONG)) {
            status = BAD_REQUEST;
            msg = msgs.getImageUrlTooLong().replace("{MAX}", ProductEntity.PRODUCT_MAX_IMG_URL_LENGTH + "");

        }  else if (e.equals(STOCK_NEGATIVE)) {
            status = BAD_REQUEST;
            msg = msgs.getStockNegative();

        } else if (e.equals(UNSUPPORTED_FILTER_ATTRIBUTE)) {
            status = BAD_REQUEST;
            msg = msgs.getUnsupportedFilterAttribute();

        } else if (e.equals(PRODUCT_NOT_FOUND_BY_ID)) {
            status = NOT_FOUND;
            msg = msgs.getProductNotFoundById();

        }  else if (e.equals(CATEGORY_NOT_FOUND_BY_ID)) {
            status = NOT_FOUND;
            msg = msgs.getCategoryNotFoundById();

        } else if (e.equals(USER_NOT_FOUND_BY_ID)) {
            status = NOT_FOUND;
            msg = msgs.getUserNotFoundById();

        } else if (e.equals(PRICE_FILTER_ERROR_PARSING_STR_TO_BIGDECIMAL)) {
            status = BAD_REQUEST;
            msg = msgs.getPriceFilterErrorParsingStrToBigDecimal();

        }  else if (e.equals(INVALID_CATEGORY_ID)) {
            status = BAD_REQUEST;
            msg = msgs.getInvalidCategoryId();

        } else {
            log.error("A custom exception should have custom response handling {}", e);
            status = INTERNAL_SERVER_ERROR;
            msg = systemErrorProperties.getUnexpectedError();
        }

        return ResponseEntity
                .status(status)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(msg));
    }


}
