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
import static org.cris6h16.product.Exceptions.ProductErrorCode.INVALID_STOCK;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_DESCRIPTION_LENGTH_MISMATCH;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_LIST_IMG_IS_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NAME_LENGTH_MISMATCH;
import static org.cris6h16.product.Exceptions.ProductErrorCode.USER_ID_NULL;

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

        if (e.equals(USER_ID_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getUserIdNull();

        } else if (e.equals(ProductErrorCode.USER_ID_LESS_THAN_ONE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getUserIdLessThanOne();

        }else if (e.equals(PRODUCT_NAME_LENGTH_MISMATCH)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getProductNameLengthMismatch();

        }else if (e.equals(PRODUCT_DESCRIPTION_LENGTH_MISMATCH)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getProductDescriptionLengthMismatch();

        } else if (e.equals(ProductErrorCode.UNIQUE_USER_ID_PRODUCT_NAME)) {
            status = HttpStatus.CONFLICT;
            msg = msgs.getUniqueUserIdProductName();

        }else if (e.equals(INVALID_STOCK)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getInvalidStock();

        }else if (e.equals(ProductErrorCode.INVALID_PRICE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getInvalidPrice();

        } else if (e.equals(ProductErrorCode.CATEGORY_ID_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getCategoryIdNull();

        } else if (e.equals(ProductErrorCode.CATEGORY_ID_LESS_THAN_ONE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getCategoryIdLessThanOne();

        } else if (e.equals(ProductErrorCode.IMAGE_URL_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getImageUrlNull();

        } else if (e.equals(ProductErrorCode.IMAGE_URL_TOO_LONG)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getImageUrlTooLong().replace("{MAX}", ProductEntity.PRODUCT_MAX_IMG_URL_LENGTH + "");

        } else if (e.equals(ProductErrorCode.APPROX_WEIGHT_LB_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getApproxWeightLbNull();

        } else if (e.equals(ProductErrorCode.APPROX_WEIGHT_LB_NEGATIVE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getApproxWeightLbNegative();

        } else if (e.equals(ProductErrorCode.STOCK_NEGATIVE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getStockNegative();

        } else if (e.equals(ProductErrorCode.STOCK_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getStockNull();

        } else if (e.equals(ProductErrorCode.APPROX_HEIGHT_CM_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getApproxHeightCmNull();

        } else if (e.equals(ProductErrorCode.APPROX_HEIGHT_CM_NEGATIVE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getApproxHeightCmNegative();

        } else if (e.equals(ProductErrorCode.APPROX_WIDTH_CM_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getApproxWidthCmNull();

        } else if (e.equals(ProductErrorCode.APPROX_WIDTH_CM_NEGATIVE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getApproxWidthCmNegative();

        } else if (e.equals(ProductErrorCode.DESCRIPTION_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getDescriptionNull();

        } else if (e.equals(ProductErrorCode.DESCRIPTION_TOO_LONG)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getDescriptionTooLong().replace("{MAX}", ProductEntity.PRODUCT_MAX_DESCRIPTION_LENGTH + "");

        } else if (e.equals(ProductErrorCode.PRICE_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getPriceNull();

        } else if (e.equals(ProductErrorCode.PRICE_NEGATIVE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getPriceNegative();

        } else if (e.equals(ProductErrorCode.PRICE_FILTER_INVALID_FORMAT)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getPriceFilterInvalidFormat();

        } else if (e.equals(ProductErrorCode.PRODUCT_NAME_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getProductNameNull();

        } else if (e.equals(ProductErrorCode.PRODUCT_NAME_TOO_LONG)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getProductNameTooLong().replace("{MAX}", ProductEntity.PRODUCT_MAX_NAME_LENGTH + "");

        } else if (e.equals(ProductErrorCode.CATEGORY_NAME_TOO_LONG)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getCategoryNameTooLong().replace("{MAX}", ProductEntity.PRODUCT_MAX_DESCRIPTION_LENGTH + "");

        } else if (e.equals(ProductErrorCode.CATEGORY_NAME_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getCategoryNameNull();

        } else if (e.equals(ProductErrorCode.PRODUCT_ID_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getProductIdNull();

        } else if (e.equals(ProductErrorCode.PRODUCT_ID_LESS_THAN_ONE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getProductIdLessThanOne();

        } else if (e.equals(ProductErrorCode.UNSUPPORTED_FILTER_ATTRIBUTE)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getUnsupportedFilterAttribute();

        } else if (e.equals(ProductErrorCode.PRODUCT_NOT_FOUND_BY_ID)) {
            status = HttpStatus.NOT_FOUND;
            msg = msgs.getProductNotFoundById();

        } else if (e.equals(ProductErrorCode.PRODUCT_LIST_IMG_IS_EMPTY) || e.equals(PRODUCT_LIST_IMG_IS_NULL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getProductListImgIsEmpty();

        } else if (e.equals(ProductErrorCode.CATEGORY_NOT_FOUND_BY_ID)) {
            status = HttpStatus.NOT_FOUND;
            msg = msgs.getCategoryNotFoundById();

        } else if (e.equals(ProductErrorCode.USER_NOT_FOUND_BY_ID)) {
            status = HttpStatus.NOT_FOUND;
            msg = msgs.getUserNotFoundById();

        } else if (e.equals(ProductErrorCode.PRICE_FILTER_ERROR_PARSING_STR_TO_BIGDECIMAL)) {
            status = HttpStatus.BAD_REQUEST;
            msg = msgs.getPriceFilterErrorParsingStrToBigDecimal();

        } else {
            log.error("A custom exception should have custom response handling {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            msg = systemErrorProperties.getUnexpectedError();
        }

        return ResponseEntity
                .status(status)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(msg));
    }


}
