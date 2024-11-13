package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxHeightCmException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxWeightLbException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxWidthCmException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidCategoryIdException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidCategoryNameLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidDescriptionLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidImageUrlLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidProductNameLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidPriceException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidStockException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidUserIdException;
import org.springframework.stereotype.Component;

import static org.cris6h16.product.CategoryEntity.CATEGORY_NAME_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_DESCRIPTION_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_IMG_URL_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_NAME_LENGTH;

@Slf4j
@Component
class ProductValidator {


    public void validate(CreateProductInput input) {
        validateString(input.getName(), PRODUCT_NAME_LENGTH, "product name", ProductInvalidProductNameLengthException.class);
        validatePositive(input.getPrice(), "product price", ProductInvalidPriceException.class);
        validateNonNegative(input.getStock(), "product stock", ProductInvalidStockException.class);
        validateString(input.getDescription(), PRODUCT_DESCRIPTION_LENGTH, "description", ProductInvalidDescriptionLengthException.class);
        validateNonNegative(input.getApproxWeightLb(), "approx weight (lbs)", ProductInvalidApproxWeightLbException.class);
        validateNonNegative(input.getApproxWidthCm(), "approx width (cm)", ProductInvalidApproxWidthCmException.class);
        validateNonNegative(input.getApproxHeightCm(), "approx height (cm)", ProductInvalidApproxHeightCmException.class);
        validateString(input.getImageUrl(), PRODUCT_IMG_URL_LENGTH, "image URL", ProductInvalidImageUrlLengthException.class);
        validatePositive(input.getCategoryId(), "category ID", ProductInvalidCategoryIdException.class);
        validatePositive(input.getUserId(), "user ID", ProductInvalidUserIdException.class);
    }

    private <E extends RuntimeException> void validateString(String value, int maxLength, String fieldName, Class<E> exceptionClass) {
        value = (value == null) ? "" : value.trim();
        if (value.isEmpty() || value.length() > maxLength) {
            log.debug("Invalid {}: {}", fieldName, value);
            throw createException(exceptionClass);
        }
    }

    private <E extends RuntimeException> void validatePositive(Number value, String fieldName, Class<E> exceptionClass) {
        if (value == null || value.longValue() <= 0) {
            log.debug("Invalid {}: {}", fieldName, value);
            throw createException(exceptionClass);
        }
    }

    private <E extends RuntimeException> void validateNonNegative(Number value, String fieldName, Class<E> exceptionClass) {
        if (value == null || value.longValue() < 0) {
            log.debug("Invalid {}: {}", fieldName, value);
            throw createException(exceptionClass);
        }
    }

    private <E extends RuntimeException> void validateNotNull(Object value, String fieldName, Class<E> exceptionClass) {
        if (value == null) {
            log.debug("Invalid {}: {}", fieldName, value);
            throw createException(exceptionClass);
        }
    }

    private <E extends RuntimeException> E createException(Class<E> exceptionClass) {
        try {
            return exceptionClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate exception: " + exceptionClass, e);
        }
    }

    void validate(CreateCategoryInput input) {
        validateString(input.getName(), CATEGORY_NAME_LENGTH, "category name", ProductInvalidCategoryNameLengthException.class);
    }
}
