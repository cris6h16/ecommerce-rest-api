package org.cris6h16;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
 class CreateUserInput implements Prepareable {
    private Long id;
    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private BigDecimal balance;
    private boolean enabled;
    private boolean emailVerified;
    private Set<EAuthority> authorities;

    @Override
    public void trim() {

    }

    @Override
    public void nullToEmpty() {

    }
}
