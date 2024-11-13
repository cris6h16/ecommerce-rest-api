package org.cris6h16.product;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.cris6h16.user.UserEntity;

import java.math.BigDecimal;
import java.util.Set;

// todo: agregar indexes en entities
@Entity(name = "products")
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "approxWeightLb", "approxWidthCm", "approxHeightCm"},
                        name = "product_unique_name_weight_width_height"
                )
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@Setter
@ToString
class ProductEntity {
    public final static int NAME_LENGTH = 255;
    public final static int DESCRIPTION_LENGTH = 1000;
    public final static int IMG_URL_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = NAME_LENGTH)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = DESCRIPTION_LENGTH)
    private String description;

    @Column(nullable = false)
    private Integer approxWeightLb;

    @Column(nullable = false)
    private Integer approxWidthCm;

    @Column(nullable = false)
    private Integer approxHeightCm;

    @Column(nullable = false, length = IMG_URL_LENGTH)
    private String imageUrl;

    @ManyToOne(
            cascade = {},
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_products_users")
    )
    private UserEntity user;


    @ManyToOne(
            cascade = {},
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "category_id",
            foreignKey = @ForeignKey(name = "fk_products_categories")
    )
    private CategoryEntity category;


    @ManyToOne(
            cascade = {},
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "brand_id",
            foreignKey = @ForeignKey(name = "fk_products_brands")
    )
    private BrandEntity brand; // todo: SN default

// at the moment, tags are not needed
//    @ElementCollection(fetch = FetchType.LAZY)
//    private Set<String> tags;
}
