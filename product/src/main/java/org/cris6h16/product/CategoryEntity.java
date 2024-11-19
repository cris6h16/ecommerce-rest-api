package org.cris6h16.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "categories")
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name"},
                        name = "category_unique_name"
                )
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@Setter
 public class CategoryEntity {
    public static final int CATEGORY_MAX_NAME_LENGTH = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = CATEGORY_MAX_NAME_LENGTH, unique = true)
    private String name;
}
