package org.cris6h16.product;

import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxHeightCmException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxWeightLbException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidApproxWidthCmException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidCategoryIdException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidCategoryNameLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidDescriptionLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidImageUrlLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidProductIdException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidProductNameLengthException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidPriceException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidStockException;
import org.cris6h16.product.Exceptions.invalid.ProductInvalidUserIdException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static org.cris6h16.product.ProductEntity.PRODUCT_DESCRIPTION_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_IMG_URL_LENGTH;
import static org.cris6h16.product.ProductEntity.PRODUCT_NAME_LENGTH;

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

    private void greaterThan(int ref, int value, String fieldName, Class<? extends RuntimeException> e) {
        if (value > ref) return;
        log.debug("The value {} is not greater than {}, field: {}", value, ref, fieldName);
        throw createException(e);
    }

    private void lessOrEqualsThan(int ref, int value, String fieldName, Class<? extends RuntimeException> e) {
        if (value <= ref) return;
        log.debug("The value {} is not less or equals than {}, field: {}", value, ref, fieldName);
        throw createException(e);
    }

    private void greaterOrEqualsThan(int ref, int value, String fieldName, Class<? extends RuntimeException> e) {
        if (value >= ref) return;
        throw createException(e);
    }

    private void greaterOrEqualsThan(BigDecimal ref, BigDecimal value, String fieldName, Class<? extends RuntimeException> e) {
//        if (value >= ref) return;
        if (value.compareTo(ref) >= 0) return;
        throw createException(e);
    }

    public void validateUserId(Long userId) {
        Class<ProductInvalidUserIdException> e = ProductInvalidUserIdException.class;

        validateNotNull(userId, "user ID", e);
        greaterThan(0, userId.intValue(), "user ID", e);
    }

    public void validateCategoryId(Long categoryId) {
        Class<ProductInvalidCategoryIdException> e = ProductInvalidCategoryIdException.class;

        validateNotNull(categoryId, "category ID", e);
        greaterThan(0, categoryId.intValue(), "category ID", e);
    }

    public void validateImageUrl(String imageUrl) {
        Class<ProductInvalidImageUrlLengthException> e = ProductInvalidImageUrlLengthException.class;

        validateNotNull(imageUrl, "image URL", e);
        lessOrEqualsThan(PRODUCT_IMG_URL_LENGTH, imageUrl.length(), "image URL", ProductInvalidImageUrlLengthException.class);
    }

    public void validateApproxHeightCm(Integer approxHeightCm) {
        Class<ProductInvalidApproxHeightCmException> e = ProductInvalidApproxHeightCmException.class;

        validateNotNull(approxHeightCm, "approx height (cm)", e);
        greaterOrEqualsThan(0, approxHeightCm, "approx height (cm)", e);
    }

    public void validateApproxWidthCm(Integer approxWidthCm) {
        Class<ProductInvalidApproxWidthCmException> e = ProductInvalidApproxWidthCmException.class;

        validateNotNull(approxWidthCm, "approx width (cm)", e);
        greaterOrEqualsThan(0, approxWidthCm, "approx width (cm)", e);
    }

    public void validateApproxWeightLb(Integer approxWeightLb) {
        Class<ProductInvalidApproxWeightLbException> e = ProductInvalidApproxWeightLbException.class;

        validateNotNull(approxWeightLb, "approx weight (lbs)", e);
        greaterOrEqualsThan(0, approxWeightLb, "approx weight (lbs)", e);
    }

    public void validateDescription(String description) {
        Class<ProductInvalidDescriptionLengthException> e = ProductInvalidDescriptionLengthException.class;

        validateNotNull(description, "product description", e);
        lessOrEqualsThan(PRODUCT_DESCRIPTION_LENGTH, description.length(), "product description", e);
    }

    // todo: refactor others validators like this component
    public void validateStock(Integer stock) {
        Class<ProductInvalidStockException> e = ProductInvalidStockException.class;

        validateNotNull(stock, "product stock", e);
        greaterOrEqualsThan(0, stock, "product stock", e);
    }

    public void validatePrice(BigDecimal price) {
        Class<ProductInvalidPriceException> e = ProductInvalidPriceException.class;

        validateNotNull(price, "product price", e);
        greaterOrEqualsThan(BigDecimal.ZERO, price, "product price",e);
    }

    public void validateProductName(String name) {
        Class<ProductInvalidProductNameLengthException> e = ProductInvalidProductNameLengthException.class;

        validateNotNull(name, "product name", e);
        lessOrEqualsThan(PRODUCT_NAME_LENGTH, name.length(),"product name", e);
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

 public    void validate(CreateCategoryInput input) {
        validateCategoryName(input.getName());
    }

    private void validateCategoryName(String name) {
        Class<ProductInvalidCategoryNameLengthException> e = ProductInvalidCategoryNameLengthException.class;

        validateNotNull(name, "category name", e);
        greaterThan(1, name.length(),"category name", e);
    }

    public void validateProductId(Long id) {
        Class<ProductInvalidProductIdException> e = ProductInvalidProductIdException.class;

        validateNotNull(id, "product ID", e);
        greaterThan(0, id.intValue(), "product ID", e);
    }
}
