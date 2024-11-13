package org.cris6h16.user;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;


/**
 * Representacion de la entidad {@code Usuario} en la base de datos
 *
 * @author <a href="https://github.com/cris6h16" target="_blank"> Cristian Manuel Herrera Guallo </a>
 */
@Entity(name = "users")
@Table(
        name = "users",
        indexes = {
                @Index(columnList = "email", name = "email_index")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "email_unique")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@Setter
public class UserEntity {
    // This statics have to be used as a centralize place for validation, not were wrote for make easy for change ( error messages contains this values )
    protected static final int FIRSTNAME_LENGTH = 30;
    protected static final int LASTNAME_LENGTH = 30;
    protected static final int EMAIL_LENGTH = 255;
    protected static final int PASSWORD_LENGTH = 80; // deberia ser 60 (bcrypt) pero la encrypción suele dejar prefijo como {bcrypt} o {noop}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = FIRSTNAME_LENGTH, name = "first_name")
    private String firstname;

    @Column(nullable = false, name = "last_name", length = LASTNAME_LENGTH)
    private String lastname;

    @Column(nullable = false, length = EMAIL_LENGTH, unique = true)
    private String email;

    @Column(nullable = false, length = PASSWORD_LENGTH)
    private String password;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false, name = "email_verified")
    private boolean emailVerified;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST}
    )
    @JoinTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"user_id", "authority_id"}, name = "unique_user_authority")
            },
            foreignKey = @ForeignKey(name = "fk_user_authority_user")
    )
    private Set<AuthorityEntity> authorities;
}
