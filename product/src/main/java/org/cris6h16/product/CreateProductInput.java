package org.cris6h16.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class CreateProductInput implements Prepareable {
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private Integer weightPounds;
    private Integer widthCM;
    private Integer heightCM;
    private Integer lengthCM;
    private Set<String> imageUrls;
    private Long categoryId;
    private Long userId;

    @Override
    public void trim() {
        name = name.trim();
        description = description.trim();
        imageUrls = Set.copyOf(imageUrls.stream().map(String::trim).toList());
    }

    @Override
    public void nullToEmpty() {
        name = name == null ? "" : name;
        price = price == null ? BigDecimal.ZERO : price;
        stock = stock == null ? 0 : stock;
        description = description == null ? "" : description;
        weightPounds = weightPounds == null ? 0 : weightPounds;
        widthCM = widthCM == null ? 0 : widthCM;
        heightCM = heightCM == null ? 0 : heightCM;
        lengthCM = lengthCM == null ? 0 : lengthCM;
        imageUrls = imageUrls == null ? Set.of() : imageUrls;
    }
}
