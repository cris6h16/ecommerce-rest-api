package org.cris6h16.product;

import jakarta.persistence.criteria.Expression;
import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.ProductComponentException;
import org.cris6h16.product.Exceptions.ProductErrorCode;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.cris6h16.product.Exceptions.ProductErrorCode.INVALID_CATEGORY_ID;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRICE_FILTER_ERROR_PARSING_STR_TO_BIGDECIMAL;
import static org.cris6h16.product.Exceptions.ProductErrorCode.PRICE_FILTER_INVALID_FORMAT;

@Slf4j
public class ProductSpecs {
    public static Specification<ProductEntity> hasNameLike(String name) {
        name = name.trim().toLowerCase();

        log.debug("ProductSpecs.hasNameLike: name={}", name);
        String finalName = name;
        return (root, query, cb) -> {
            Expression<String> productName = cb.lower(cb.function("public.unaccent", String.class, root.get("name")));
            Expression<String> searchName = cb.lower(cb.function("public.unaccent", String.class, cb.literal("%" + finalName + "%")));

            log.debug("ProductSpecs.hasNameLike: productName={}, searchName={}", productName.toString(), searchName.toString());
            return cb.like(productName, searchName);
        };
//        return ((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%"));
    }

    public static Specification<ProductEntity> hasDescriptionLike(String description) {
        description = description.trim().toLowerCase();

        String finalDescription = description;
        return (root, query, cb) -> {
            Expression<String> productDescription = cb.lower(cb.function("public.unaccent", String.class, root.get("description")));
            Expression<String> searchDescription = cb.lower(cb.function("public.unaccent", String.class, cb.literal("%" + finalDescription + "%")));

            return cb.like(productDescription, searchDescription);
        };
    }


    public static Specification<ProductEntity> hasCategoryId(String categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), parseLong(categoryId, INVALID_CATEGORY_ID));
    }

    private static Long parseLong(String categoryId, ProductErrorCode errorCode) {
        try {
            return Long.parseLong(categoryId);

        } catch (NumberFormatException e) {
            throw new ProductComponentException(errorCode);
        }
    }

    public static Specification<ProductEntity> hasPrice(String priceStr) {
        return ((root, query, cb) -> {
            if (priceStr.matches("^>\\d+$")) { // price > X
                return cb.greaterThan(root.get("price"), parsePrice(priceStr, 1));
            }

            if (priceStr.matches("^<\\d+$")) { // price < X
                return cb.lessThan(root.get("price"), parsePrice(priceStr, 1));
            }

            throw new ProductComponentException(PRICE_FILTER_INVALID_FORMAT);
        });
    }

    private static BigDecimal parsePrice(String priceStr, int offset) {
        try {
            return new BigDecimal(priceStr.substring(offset));
        } catch (NumberFormatException e) {
            log.error("Error parsing price filter: {}", priceStr);
            throw new ProductComponentException(PRICE_FILTER_ERROR_PARSING_STR_TO_BIGDECIMAL);
        }
    }

}
