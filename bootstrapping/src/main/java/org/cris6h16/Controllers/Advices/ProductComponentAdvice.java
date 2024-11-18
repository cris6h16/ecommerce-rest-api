package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.ProductComponentErrorMsgProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.product.CategoryEntity;
import org.cris6h16.product.Exceptions.ProductComponentAlreadyExistsException;
import org.cris6h16.product.Exceptions.ProductComponentInvalidAttributeException;
import org.cris6h16.product.Exceptions.ProductComponentNotFoundException;
import org.cris6h16.product.Exceptions.ProductErrorCode;
import org.cris6h16.product.ProductEntity;
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
public class ProductComponentAdvice {
    private final ProductComponentErrorMsgProperties msgs;
    private final SystemErrorProperties systemErrorProperties;

    public ProductComponentAdvice(ProductComponentErrorMsgProperties productErrorMsgProperties, SystemErrorProperties systemErrorProperties) {
        this.msgs = productErrorMsgProperties;
        this.systemErrorProperties = systemErrorProperties;
    }

    @ExceptionHandler(ProductComponentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductComponentAlreadyExistsException(ProductComponentAlreadyExistsException e) {
        log.debug("ProductComponentAlreadyExistsException", e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(e.getErrorCode().name(), getMsg(e.getErrorCode())));
    }

    @ExceptionHandler(ProductComponentInvalidAttributeException.class)
    public ResponseEntity<ErrorResponse> handleProductComponentInvalidAttributeException(ProductComponentInvalidAttributeException e) {
        log.debug("ProductComponentInvalidAttributeException", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(e.getErrorCode().name(), getMsg(e.getErrorCode())));
    }


    @ExceptionHandler(ProductComponentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductComponentNotFoundException(ProductComponentNotFoundException e) {
        log.debug("ProductComponentNotFoundException", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(e.getErrorCode().name(), getMsg(e.getErrorCode())));
    }

    private String getMsg(ProductErrorCode e) {

        if (e.equals(ProductErrorCode.USER_ID_NULL)) {
            return msgs.getUserIdNull();
        }
        if (e.equals(ProductErrorCode.USER_ID_LESS_THAN_ONE)) {
            return msgs.getUserIdLessThanOne();
        }

        if (e.equals(ProductErrorCode.CATEGORY_ID_NULL)) {
            return msgs.getCategoryIdNull();
        }
        if (e.equals(ProductErrorCode.CATEGORY_ID_LESS_THAN_ONE)) {
            return msgs.getCategoryIdLessThanOne();
        }

        if (e.equals(ProductErrorCode.IMAGE_URL_NULL)) {
            return msgs.getImageUrlNull();
        }

        if (e.equals(ProductErrorCode.IMAGE_URL_TOO_LONG)) {
            return msgs.getImageUrlTooLong().replace("{MAX}", ProductEntity.PRODUCT_MAX_IMG_URL_LENGTH + "");
        }

        if (e.equals(ProductErrorCode.APPROX_WEIGHT_LB_NULL)) {
            return msgs.getApproxWeightLbNull();
        }

        if (e.equals(ProductErrorCode.STOCK_NEGATIVE)) {
            return msgs.getStockNegative();
        }

        if (e.equals(ProductErrorCode.APPROX_HEIGHT_CM_NULL)) {
            return msgs.getApproxHeightCmNull();
        }

        if (e.equals(ProductErrorCode.APPROX_HEIGHT_CM_NEGATIVE)) {
            return msgs.getApproxHeightCmNegative();
        }

        if (e.equals(ProductErrorCode.APPROX_WIDTH_CM_NULL)) {
            return msgs.getApproxWidthCmNull();
        }

        if (e.equals(ProductErrorCode.APPROX_WIDTH_CM_NEGATIVE)) {
            return msgs.getApproxWidthCmNegative();
        }

        if (e.equals(ProductErrorCode.APPROX_WEIGHT_LB_NEGATIVE)) {
            return msgs.getApproxWeightLbNegative();
        }
        if (e.equals(ProductErrorCode.DESCRIPTION_NULL)) {
            return msgs.getDescriptionNull();
        }
        if (e.equals(ProductErrorCode.DESCRIPTION_TOO_LONG)) {
            return msgs.getDescriptionTooLong().replace("{MAX}", ProductEntity.PRODUCT_MAX_DESCRIPTION_LENGTH + "");
        }

        if (e.equals(ProductErrorCode.STOCK_NULL)) {
            return msgs.getStockNull();
        }

        if (e.equals(ProductErrorCode.PRICE_NULL)) {
            return msgs.getPriceNull();
        }
        if (e.equals(ProductErrorCode.PRICE_NEGATIVE)) {
            return msgs.getPriceNegative();
        }
        if (e.equals(ProductErrorCode.PRODUCT_NAME_NULL)) {
            return msgs.getProductNameNull();
        }

        if (e.equals(ProductErrorCode.PRODUCT_NAME_TOO_LONG)) {
            return msgs.getProductNameTooLong().replace("{MAX}", ProductEntity.PRODUCT_MAX_NAME_LENGTH + "");
        }

        if (e.equals(ProductErrorCode.CATEGORY_NAME_TOO_LONG)) {
            return msgs.getCategoryNameTooLong().replace("{MAX}", ProductEntity.PRODUCT_MAX_DESCRIPTION_LENGTH + "");
        }

        if (e.equals(ProductErrorCode.PRODUCT_ID_NULL)) {
            return msgs.getProductIdNull();
        }

        if (e.equals(ProductErrorCode.PRODUCT_ID_LESS_THAN_ONE)) {
            return msgs.getProductIdLessThanOne();
        }
        if (e.equals(ProductErrorCode.UNIQUE_USER_ID_PRODUCT_NAME)) {
            return msgs.getUniqueUserIdProductName();
        }

        if (e.equals(ProductErrorCode.CATEGORY_NAME_NULL)) {
            return msgs.getCategoryNameNull();
        }

        if (e.equals(ProductErrorCode.CATEGORY_NOT_FOUND_BY_ID)) {
            return msgs.getCategoryNotFoundById();
        }

        if (e.equals(ProductErrorCode.USER_NOT_FOUND_BY_ID)) {
            return msgs.getUserNotFoundById();
        }


        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }


}
