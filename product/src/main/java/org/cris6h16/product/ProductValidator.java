package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.ProductComponentException;
import org.cris6h16.product.Exceptions.ProductErrorCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.cris6h16.product.CategoryEntity.CATEGORY_MAX_NAME_LENGTH;
import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_ID_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.CATEGORY_NAME_TOO_LONG;
import static org.cris6h16.product.Exceptions.ProductErrorCode.HEIGHT_CM_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.IMAGE_URL_TOO_LONG;
import static org.cris6h16.product.Exceptions.ProductErrorCode.INVALID_PRICE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.LENGTH_CM_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_DESCRIPTION_LENGTH_MISMATCH;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_HAS_NO_IMAGES;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_ID_INVALID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRODUCT_NAME_LENGTH_MISMATCH;
import static org.cris6h16.product.Exceptions.ProductErrorCode.STOCK_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.USER_ID_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.WEIGHT_POUNDS_NEGATIVE;
import static org.cris6h16.product.Exceptions.ProductErrorCode.WIDTH_CM_NEGATIVE;
import static org.cris6h16.product.ProductEntity.PRODUCT_MAX_DESCRIPTION_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_MAX_IMG_URL_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_MAX_NAME_LENGTH;

@Slf4j
@Component
class ProductValidator {


    public void validate(CreateProductInput input) {
        input.setName(validateProductName(input.getName()));
        input.setDescription(validateDescription(input.getDescription()));
        input.setPrice(validatePrice(input.getPrice()));
        input.setStock(validateStock(input.getStock()));
        input.setWeightPounds(validateWeightPounds(input.getWeightPounds()));
        input.setWidthCM(validateWidthCM(input.getWidthCM()));
        input.setHeightCM(validateHeightCM(input.getHeightCM()));
        input.setLengthCM(validateLengthCM(input.getLengthCM()));
        input.setImageUrls(validateImagesUrl(input.getImageUrls()));
    }

    private Integer validateLengthCM(Integer lengthCM) {
        return validateNonNegative(lengthCM, LENGTH_CM_NEGATIVE);
    }


    public Long validateUserId(Long userId) {
        return validateNonNegative(userId, USER_ID_NEGATIVE);
    }

    public Long validateCategoryId(Long categoryId) {
        return validateNonNegative(categoryId, CATEGORY_ID_NEGATIVE);
    }


    public Integer validateHeightCM(Integer approxHeightCm) {
        return validateNonNegative(approxHeightCm, HEIGHT_CM_NEGATIVE);
    }

    public Integer validateWidthCM(Integer wCM) {
        return validateNonNegative(wCM, WIDTH_CM_NEGATIVE);
    }

    public BigDecimal validateWeightPounds(BigDecimal wp) {
        wp = wp == null ? BigDecimal.ZERO : wp;
        if (!(wp.compareTo(BigDecimal.ZERO) > 0)) throwE(WEIGHT_POUNDS_NEGATIVE);
        return wp;
    }

    public String validateDescription(String description) {
        return validateLength(description, 1, PRODUCT_MAX_DESCRIPTION_LENGTH, PRODUCT_DESCRIPTION_LENGTH_MISMATCH);
    }

    public Integer validateStock(Integer stock) {
        return validateNonNegative(stock, STOCK_NEGATIVE);
    }

    public BigDecimal validatePrice(BigDecimal price) {
        price = price == null ? BigDecimal.ZERO : price;
        if (!(price.compareTo(BigDecimal.ZERO) > 0)) throwE(INVALID_PRICE); // price must be greater than 0
        return price;
    }

    public String validateProductName(String name) {
        return validateLength(name, 1, PRODUCT_MAX_NAME_LENGTH, PRODUCT_NAME_LENGTH_MISMATCH);
    }

    private String validateLength(String name, int minLength, int maxLength, ProductErrorCode lengthError) {
        name = trim(name);
        if (name.length() < minLength || name.length() > maxLength) throwE(lengthError);
        return name;
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }


    private void throwE(ProductErrorCode errorCode) {
        throw new ProductComponentException(errorCode);
    }

    public void validate(CreateCategoryInput input) {
        validateCategoryName(input.getName());
    }

    private String validateCategoryName(String name) {
        name = trim(name);
        if (name.length() > CATEGORY_MAX_NAME_LENGTH) throwE(CATEGORY_NAME_TOO_LONG);
        return name;
    }

    public void validateProductId(Long id) {
        if (id == null || id < 1) throwE(PRODUCT_ID_INVALID);
    }

    Set<String> validateImagesUrl(Set<String> urls) {
        Set<String> emptySet = new HashSet<>();
        if (urls == null || urls.isEmpty()) throwE(PRODUCT_HAS_NO_IMAGES);

        for (String url : urls) {
            emptySet.add(validateImageUrl(url));
        }

        return emptySet;
    }

    public String validateImageUrl(String url) {
        url = trim(url);
        if (url.length() > PRODUCT_MAX_IMG_URL_LENGTH) throwE(IMAGE_URL_TOO_LONG);
        return url;
    }


    private Integer validateNonNegative(Integer value, ProductErrorCode errorCode) {
        value = value == null ? 0 : value;
        if (value < 0) throwE(errorCode);
        return value;
    }

    private Long validateNonNegative(Long value, ProductErrorCode errorCode) {
        value = value == null ? 0 : value;
        if (value < 0) throwE(errorCode);
        return value;
    }
}
