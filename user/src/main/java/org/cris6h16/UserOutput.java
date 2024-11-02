package org.cris6h16;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Builder
public class UserOutput {
    private Long id;
    private String firstname;
    private String lastname;
    @Getter
    private String email;
    @Getter
    private String password;
    private BigDecimal balance;
    @Getter
    private boolean enabled;
    @Getter
    private boolean emailVerified;
    private Set<AuthorityEntity> authorities;
}
