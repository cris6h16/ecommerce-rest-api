package org.cris6h16.user;

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
    private String authority;
    private boolean enabled;
    private BigDecimal balance;
    private boolean emailVerified;
}
