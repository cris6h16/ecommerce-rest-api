package org.cris6h16.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


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
  class UserEntity {
    protected static final int FIRSTNAME_MAX_LENGTH = 30;
    protected static final int LASTNAME_MAX_LENGTH = 30;
    protected static final int EMAIL_MAX_LENGTH = 255;
    protected static final int PASSWORD_MAX_LENGTH = 80; // deberia ser 60 (bcrypt) pero la encrypción suele dejar prefijo como {bcrypt} o {noop}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = FIRSTNAME_MAX_LENGTH, name = "first_name")
    private String firstname;

    @Column(nullable = false, name = "last_name", length = LASTNAME_MAX_LENGTH)
    private String lastname;

    @Column(nullable = false, length = EMAIL_MAX_LENGTH, unique = true)
    private String email;

    @Column(nullable = false, length = PASSWORD_MAX_LENGTH)
    private String password;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false, name = "email_verified")
    private boolean emailVerified;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @Enumerated(EnumType.STRING)
//    @CollectionTable(
//            name = "authorities",
//            joinColumns = @JoinColumn(name = "user_id"),
//            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "authority"})
//    )
//  private Set<EAuthority> authorities;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EAuthority authority;
}
