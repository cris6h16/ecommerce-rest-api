package org.cris6h16.Controllers.Advices;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.Controllers.Advices.Properties.ProductComponentErrorMsgProperties;
import org.cris6h16.Controllers.Advices.Properties.SystemErrorProperties;
import org.cris6h16.product.Exceptions.NotFound.ProductCategoryNotFoundException;
import org.cris6h16.product.Exceptions.ProductComponentNotFoundException;
import org.cris6h16.product.Exceptions.NotFound.ProductUserNotFoundException;
import org.cris6h16.product.Exceptions.ProductAlreadyExistsException;
import org.cris6h16.product.Exceptions.alreadyExists.ProductUserAlreadyHasAProductWithTheSpecifiedNameException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxHeightCmException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxWeightLbException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxWidthCmException;
import org.cris6h16.product.Exceptions.ProductInvalidAttributeException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidCategoryIdException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidCategoryNameLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidDescriptionLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidImageUrlLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidPriceException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidProductIdException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidProductNameLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidStockException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidUserIdException;
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
    private final ProductComponentErrorMsgProperties productComponentErrorMsgProperties;
    private final SystemErrorProperties systemErrorProperties;

    public ProductComponentAdvice(ProductComponentErrorMsgProperties productErrorMsgProperties, SystemErrorProperties systemErrorProperties) {
        this.productComponentErrorMsgProperties = productErrorMsgProperties;
        this.systemErrorProperties = systemErrorProperties;
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(ProductAlreadyExistsException e) {
        log.debug("ProductAlreadyExistsException", e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(getMsg(e)));
    }


    private String getMsg(ProductAlreadyExistsException e) {
        if (e instanceof ProductUserAlreadyHasAProductWithTheSpecifiedNameException) {
            return productComponentErrorMsgProperties.getUserAlreadyHasAProductWithTheSpecifiedName();
        }
        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }

    @ExceptionHandler(ProductInvalidAttributeException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(ProductInvalidAttributeException e) {
        log.debug("ProductInvalidAttributeException", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(getMsg(e)));
    }

    private String getMsg(ProductInvalidAttributeException e) {
        if (e instanceof ProductInvalidApproxHeightCmException) {
            return productComponentErrorMsgProperties.getInvalidApproxHeightCm();
        }
        if (e instanceof ProductInvalidApproxWeightLbException) {
            return productComponentErrorMsgProperties.getInvalidApproxWeightLb();
        }
        if (e instanceof ProductInvalidApproxWidthCmException) {
            return productComponentErrorMsgProperties.getInvalidApproxWidthCm();
        }
        if (e instanceof ProductInvalidCategoryIdException) {
            return productComponentErrorMsgProperties.getInvalidCategoryId();
        }

        if (e instanceof ProductInvalidCategoryNameLengthException) {
            return productComponentErrorMsgProperties.getInvalidCategoryNameLength();
        }
        if (e instanceof ProductInvalidDescriptionLengthException) {
            return productComponentErrorMsgProperties.getInvalidDescriptionLength();
        }
        if (e instanceof ProductInvalidImageUrlLengthException) {
            return productComponentErrorMsgProperties.getInvalidImageUrlLength();
        }
        if (e instanceof ProductInvalidPriceException) {
            return productComponentErrorMsgProperties.getInvalidPrice();
        }
        if (e instanceof ProductInvalidProductIdException) {
            return productComponentErrorMsgProperties.getInvalidProductId();
        }
        if (e instanceof ProductInvalidProductNameLengthException) {
            return productComponentErrorMsgProperties.getInvalidProductNameLength();
        }
        if (e instanceof ProductInvalidStockException) {
            return productComponentErrorMsgProperties.getInvalidStock();
        }
        if (e instanceof ProductInvalidUserIdException) {
            return productComponentErrorMsgProperties.getInvalidUserId();
        }

        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }

    @ExceptionHandler(ProductComponentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(ProductComponentNotFoundException e) {
        log.debug("ProductAlreadyExistsException", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .headers(jsonHeaderCons)
                .body(new ErrorResponse(getMsg(e)));
    }

    private String getMsg(ProductComponentNotFoundException e) {
        if (e instanceof ProductCategoryNotFoundException) {
            return productComponentErrorMsgProperties.getCategoryNotFound();
        }
        if (e instanceof ProductUserNotFoundException) {
            return productComponentErrorMsgProperties.getUserNotFound();
        }

        log.error("A custom exception should have a custom message", e);
        return systemErrorProperties.getUnexpectedError();
    }


}
