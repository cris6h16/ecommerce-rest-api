package org.cris6h16.product;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.cris6h16.product.Exceptions.ProductComponentInvalidAttributeException;
import org.cris6h16.product.Exceptions.ProductErrorCode;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

@Slf4j
public class ProductSpecs {
    public static Specification<ProductEntity> hasNameLike(String name) {
        name = name.trim().toLowerCase();

        log.debug("ProductSpecs.hasNameLike: name={}", name);
        String finalName = name;
        return (root, query, cb) -> {
            Expression<String> productName = cb.lower(cb.function("unaccent", String.class, root.get("name")));
            Expression<String> searchName = cb.lower(cb.function("unaccent", String.class, cb.literal(finalName.trim().toLowerCase())));

            log.debug("ProductSpecs.hasNameLike: productName={}, searchName={}", productName.toString(), searchName.toString());
            return cb.like(productName, "%" + searchName + "%");
        };
//        return ((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%"));
    }

    public static Specification<ProductEntity> hasPrice(String priceStr) {
        return ((root, query, cb) -> {

            if (priceStr.startsWith(">=")) { // price >= X
                return cb.greaterThanOrEqualTo(root.get("price"), parsePrice(priceStr, 2));
            }

            if (priceStr.startsWith("<=")) { // price <= X
                return cb.lessThanOrEqualTo(root.get("price"), parsePrice(priceStr, 2));
            }

            if (priceStr.startsWith(">")) { // price > X
                return cb.greaterThan(root.get("price"), parsePrice(priceStr, 1));
            }

            if (priceStr.startsWith("<")) {  // price < X
                return cb.lessThan(root.get("price"), parsePrice(priceStr, 1));
            }

            log.debug("Invalid price filter format: {}", priceStr);
            throw new ProductComponentInvalidAttributeException(ProductErrorCode.PRICE_FILTER_INVALID_FORMAT);
        });
    }

    private static BigDecimal parsePrice(String priceStr, int offset) {
        try {
            return new BigDecimal(priceStr.substring(offset));
        } catch (NumberFormatException e) {
            log.error("Error parsing price filter: {}", priceStr);
            throw new ProductComponentInvalidAttributeException(ProductErrorCode.PRICE_FILTER_ERROR_PARSING);
        }
    }

}
