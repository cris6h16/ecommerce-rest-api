package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.ErrorCode;
import org.cris6h16.product.Exceptions.ProductInvalidAttributeException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.cris6h16.product.CategoryEntity.CATEGORY_MAX_NAME_LENGTH;
import static org.cris6h16.product.Exceptions.ErrorCode.APPROX_HEIGHT_CM_IS_NEGATIVE;
import static org.cris6h16.product.Exceptions.ErrorCode.APPROX_HEIGHT_CM_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.APPROX_WEIGHT_LB_NEGATIVE;
import static org.cris6h16.product.Exceptions.ErrorCode.APPROX_WEIGHT_LB_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.APPROX_WIDTH_CM_IS_NEGATIVE;
import static org.cris6h16.product.Exceptions.ErrorCode.APPROX_WIDTH_CM_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.CATEGORY_ID_LESS_THAN_ONE;
import static org.cris6h16.product.Exceptions.ErrorCode.CATEGORY_ID_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.CATEGORY_NAME_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.CATEGORY_NAME_TOO_LONG;
import static org.cris6h16.product.Exceptions.ErrorCode.DESCRIPTION_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.DESCRIPTION_TOO_LONG;
import static org.cris6h16.product.Exceptions.ErrorCode.IMAGE_URL_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.IMAGE_URL_TOO_LONG;
import static org.cris6h16.product.Exceptions.ErrorCode.PRICE_NEGATIVE;
import static org.cris6h16.product.Exceptions.ErrorCode.PRICE_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.PRODUCT_ID_LESS_THAN_ONE;
import static org.cris6h16.product.Exceptions.ErrorCode.PRODUCT_ID_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.PRODUCT_NAME_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.PRODUCT_NAME_TOO_LONG;
import static org.cris6h16.product.Exceptions.ErrorCode.STOCK_NEGATIVE;
import static org.cris6h16.product.Exceptions.ErrorCode.STOCK_NULL;
import static org.cris6h16.product.Exceptions.ErrorCode.USER_ID_LESS_THAN_ONE;
import static org.cris6h16.product.Exceptions.ErrorCode.USER_ID_NULL;
import static org.cris6h16.product.ProductEntity.PRODUCT_MAX_DESCRIPTION_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_MAX_IMG_URL_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_MAX_NAME_LENGTH;

@Slf4j
@Component
class ProductValidator {


    public void validate(CreateProductInput input) {
        validateProductName(input.getName());
        validatePrice(input.getPrice());
        validateStock(input.getStock());
        validateDescription(input.getDescription());
        validateApproxWeightLb(input.getApproxWeightLb());
        validateApproxWidthCm(input.getApproxWidthCm());
        validateApproxHeightCm(input.getApproxHeightCm());
        validateImageUrl(input.getImageUrl());
        validateCategoryId(input.getCategoryId());
        validateUserId(input.getUserId());
    }


    public void validateUserId(Long userId) {
        if (userId == null) throwE(USER_ID_NULL);
        if (userId < 1) throwE(USER_ID_LESS_THAN_ONE);
    }

    public void validateCategoryId(Long categoryId) {
        if (categoryId == null) throwE(CATEGORY_ID_NULL);
        if (categoryId < 1) throwE(CATEGORY_ID_LESS_THAN_ONE);
    }

    public void validateImageUrl(String imageUrl) {
        if (imageUrl == null) throwE(IMAGE_URL_NULL);
        if (imageUrl.length() > PRODUCT_MAX_IMG_URL_LENGTH) throwE(IMAGE_URL_TOO_LONG);
    }

    public void validateApproxHeightCm(Integer approxHeightCm) {
        if (approxHeightCm == null) throwE(APPROX_HEIGHT_CM_NULL);
        if (approxHeightCm < 0) throwE(APPROX_HEIGHT_CM_IS_NEGATIVE);
    }

    public void validateApproxWidthCm(Integer approxWidthCm) {
        if (approxWidthCm == null) throwE(APPROX_WIDTH_CM_NULL);
        if (approxWidthCm < 0) throwE(APPROX_WIDTH_CM_IS_NEGATIVE);
    }

    public void validateApproxWeightLb(Integer approxWeightLb) {
        if (approxWeightLb == null) throwE(APPROX_WEIGHT_LB_NULL);
        if (approxWeightLb < 0) throwE(APPROX_WEIGHT_LB_NEGATIVE);
    }

    public void validateDescription(String description) {
        if (description == null) throwE(DESCRIPTION_NULL);
        if (description.length() > PRODUCT_MAX_DESCRIPTION_LENGTH) throwE(DESCRIPTION_TOO_LONG);
    }

    public void validateStock(Integer stock) {
        if (stock == null) throwE(STOCK_NULL);
        if (stock < 0) throwE(STOCK_NEGATIVE);
    }

    public void validatePrice(BigDecimal price) {
        if (price == null) throwE(PRICE_NULL);
        if (price.compareTo(BigDecimal.ZERO) < 0) throwE(PRICE_NEGATIVE);
    }

    public void validateProductName(String name) {
        if (name == null) throwE(PRODUCT_NAME_NULL);
        if (name.length() > PRODUCT_MAX_NAME_LENGTH) throwE(PRODUCT_NAME_TOO_LONG);
    }


    private void throwE(ErrorCode errorCode) {
        throw new ProductInvalidAttributeException(errorCode);
    }

    public void validate(CreateCategoryInput input) {
        validateCategoryName(input.getName());
    }

    private void validateCategoryName(String name) {
        if (name == null) throwE (CATEGORY_NAME_NULL);
        if (name.length() > CATEGORY_MAX_NAME_LENGTH) throwE(CATEGORY_NAME_TOO_LONG);
    }

    public void validateProductId(Long id) {
        if (id == null) throwE(PRODUCT_ID_NULL);
        if (id < 1) throwE(PRODUCT_ID_LESS_THAN_ONE);
    }
}
