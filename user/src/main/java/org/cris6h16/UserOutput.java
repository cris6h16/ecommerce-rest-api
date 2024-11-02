package org.cris6h16;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Set;

@Builder
    @Getter
public class UserOutput {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private BigDecimal balance;
    private boolean enabled;
    private boolean emailVerified;
    private Set<String> authorities;
}
