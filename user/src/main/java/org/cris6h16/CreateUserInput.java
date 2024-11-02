package org.cris6h16;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
public class CreateUserInput implements Prepareable {
    private String firstname;
    private String lastname;
    private String email;
    @Setter
    private String password;
    private BigDecimal balance;
    private boolean enabled;
    private boolean emailVerified;
    private Set<AuthorityEntity> authorities;

    @Override
    public void trim() {
        firstname = firstname.trim();
        lastname = lastname.trim();
        email = email.trim();
        password = password.trim();
    }

    @Override
    public void nullToEmpty() {
        firstname = (firstname == null)? "" : firstname;
        lastname = (lastname == null)? "" : lastname;
        email = (email == null)? "" : email;
        password = (password == null)? "" : password;
        authorities = (authorities == null)? new HashSet<>(0) : authorities;
        balance = (balance == null)? BigDecimal.ZERO : balance;
    }
}
