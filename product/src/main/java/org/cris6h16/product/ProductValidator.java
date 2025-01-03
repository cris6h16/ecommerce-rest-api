package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.ProductComponentException;
import org.cris6h16.product.Exceptions.ProductErrorCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.cris6h16.product.CategoryEntity.CATEGORY_MAX_NAME_LENGTH;
import static org.cris6h16.product.Exceptions.ProductErrorCode.APPROX_HEIGHT_CM_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.APPROX_HEIGHT_CM_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.APPROX_WEIGHT_LB_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.APPROX_WEIGHT_LB_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.APPROX_WIDTH_CM_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.APPROX_WIDTH_CM_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_ID_LESS_THAN_ONE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_ID_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_NAME_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_NAME_TOO_LONG;
import static org.cris6h16.product.Exceptions.ProductErrorCode.DESCRIPTION_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.DESCRIPTION_TOO_LONG;
import static org.cris6h16.product.Exceptions.ProductErrorCode.IMAGE_URL_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.IMAGE_URL_TOO_LONG;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRICE_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRICE_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_ID_LESS_THAN_ONE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_ID_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NAME_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NAME_TOO_LONG;
import static org.cris6h16.product.Exceptions.ProductErrorCode.STOCK_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.STOCK_NULL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.USER_ID_LESS_THAN_ONE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.USER_ID_NULL;
import static org.cris6h16.product.ProductEntity.PRODUCT_MAX_DESCRIPTION_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_MAX_IMG_URL_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_MAX_NAME_LENGTH;

@Slf4j
@Component
class ProductValidator {


    public void validate(CreateProductInput input) {
        input.setName(validateProductName(input.getName()));
        input.setDescription(validateDescription(input.getDescription()));
        input.setImageUrls(validateImagesUrl(input.getImageUrls()));
        validatePrice(input.getPrice());
        validateStock(input.getStock());
        validateApproxWeightLb(input.getApproxWeightLb());
        validateApproxWidthCm(input.getApproxWidthCm());
        validateApproxHeightCm(input.getApproxHeightCm());
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

    public String validateImageUrl(String imageUrl) {
        if (imageUrl == null) throwE(IMAGE_URL_NULL);
        if ((imageUrl.trim()).length() > PRODUCT_MAX_IMG_URL_LENGTH) throwE(IMAGE_URL_TOO_LONG);
        return imageUrl;
    }

    public void validateApproxHeightCm(Integer approxHeightCm) {
        if (approxHeightCm == null) throwE(APPROX_HEIGHT_CM_NULL);
        if (approxHeightCm < 0) throwE(APPROX_HEIGHT_CM_NEGATIVE);
    }

    public void validateApproxWidthCm(Integer approxWidthCm) {
        if (approxWidthCm == null) throwE(APPROX_WIDTH_CM_NULL);
        if (approxWidthCm < 0) throwE(APPROX_WIDTH_CM_NEGATIVE);
    }

    public void validateApproxWeightLb(Integer approxWeightLb) {
        if (approxWeightLb == null) throwE(APPROX_WEIGHT_LB_NULL);
        if (approxWeightLb < 0) throwE(APPROX_WEIGHT_LB_NEGATIVE);
    }

    public String validateDescription(String description) {
        if (description == null) throwE(DESCRIPTION_NULL);
        if ((description.trim()).length() > PRODUCT_MAX_DESCRIPTION_LENGTH) throwE(DESCRIPTION_TOO_LONG);
        return description;
    }

    public void validateStock(Integer stock) {
        if (stock == null) throwE(STOCK_NULL);
        if (stock < 0) throwE(STOCK_NEGATIVE);
    }

    public void validatePrice(BigDecimal price) {
        if (price == null) throwE(PRICE_NULL);
        if (price.compareTo(BigDecimal.ZERO) < 0) throwE(PRICE_NEGATIVE);
    }

    public String validateProductName(String name) {
        if (name == null) throwE(PRODUCT_NAME_NULL);
        if ((name.trim()).length() > PRODUCT_MAX_NAME_LENGTH) throwE(PRODUCT_NAME_TOO_LONG);
        return name;
    }


    private void throwE(ProductErrorCode errorCode) {
        throw new ProductComponentException(errorCode);
    }

    public void validate(CreateCategoryInput input) {
        validateCategoryName(input.getName());
    }

    private void validateCategoryName(String name) {
        if (name == null) throwE(CATEGORY_NAME_NULL);
        if (name.length() > CATEGORY_MAX_NAME_LENGTH) throwE(CATEGORY_NAME_TOO_LONG);
    }

    public void validateProductId(Long id) {
        if (id == null) throwE(PRODUCT_ID_NULL);
        if (id < 1) throwE(PRODUCT_ID_LESS_THAN_ONE);
    }

    Set<String> validateImagesUrl(Set<String> urls) {
        Set<String> l = new HashSet<>();
        if (urls == null) throwE(ProductErrorCode.PRODUCT_LIST_IMG_IS_NULL);
        if (urls.isEmpty()) throwE(ProductErrorCode.PRODUCT_LIST_IMG_IS_EMPTY);

        for (String url : urls) l.add(validateImageUrl(url));

        return l;
    }
}
