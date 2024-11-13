package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxHeightCmException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxWeightLbException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxWidthCmException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidBrandIdException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidCategoryIdException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidDescriptionLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidImageUrlLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidNameLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidPriceException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidStockException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidUserIdException;

import static org.cris6h16.product.ProductEntity.DESCRIPTION_LENGTH;
import static org.cris6h16.product.ProductEntity.IMG_URL_LENGTH;
import static org.cris6h16.product.ProductEntity.NAME_LENGTH;

@Slf4j
class ProductValidator {


    public void validate(CreateProductInput input) {
        validateString(input.getName(), NAME_LENGTH, "product name", ProductInvalidNameLengthException.class);
        validatePositive(input.getPrice(), "product price", ProductInvalidPriceException.class);
        validateNonNegative(input.getStock(), "product stock", ProductInvalidStockException.class);
        validateString(input.getDescription(), DESCRIPTION_LENGTH, "description", ProductInvalidDescriptionLengthException.class);
        validateNonNegative(input.getApproxWeightLb(), "approx weight (lbs)", ProductInvalidApproxWeightLbException.class);
        validateNonNegative(input.getApproxWidthCm(), "approx width (cm)", ProductInvalidApproxWidthCmException.class);
        validateNonNegative(input.getApproxHeightCm(), "approx height (cm)", ProductInvalidApproxHeightCmException.class);
        validateString(input.getImageUrl(), IMG_URL_LENGTH, "image URL", ProductInvalidImageUrlLengthException.class);
        validatePositive(input.getCategoryId(), "category ID", ProductInvalidCategoryIdException.class);
        validatePositive(input.getUserId(), "user ID", ProductInvalidUserIdException.class);
        validatePositive(input.getBrandId(), "brand ID", ProductInvalidBrandIdException.class);
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
}
