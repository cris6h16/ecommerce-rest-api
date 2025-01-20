package org.cris6h16.product;

import jakarta.persistence.CollectionTable;
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
                        columnNames = {"name", "user_id"},
                        name = "products_unique_name_user_id"
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
public class ProductEntity {
    // todo: se puede centrilizar en un YAML para que los errores se actualizen dinamicamente
    public final static int PRODUCT_MAX_NAME_LENGTH = 255; // si se cambia esto, hay que su error message correspondiente
    public final static int PRODUCT_MAX_DESCRIPTION_LENGTH = 1000; // si se cambia esto, hay que su error message correspondiente
    public final static int PRODUCT_MAX_IMG_URL_LENGTH = 1000;// si se cambia esto, hay que su error message correspondiente

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = PRODUCT_MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = PRODUCT_MAX_DESCRIPTION_LENGTH)
    private String description;

    @Column(nullable = false)
    private Integer approxWeightLb;

    @Column(nullable = false)
    private Integer approxWidthCm;

    @Column(nullable = false)
    private Integer approxHeightCm;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "product_images",
            joinColumns = @JoinColumn(name = "entity_id"),
            foreignKey = @ForeignKey(name = "fk_product_images_products"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            columnNames = {"entity_id", "url"},
                            name = "product_images_unique_entity_id_url"
                    )
            }
    )
    @Column(name = "url", nullable = false, length = PRODUCT_MAX_IMG_URL_LENGTH) // collection table
    private Set<String> imageUrls;

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
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "category_id",
            foreignKey = @ForeignKey(name = "fk_products_categories")
    )
    private CategoryEntity category;

}
