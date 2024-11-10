package org.cris6h16.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@ToString
    @Setter
public class CreateUserInput implements Prepareable {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private boolean enabled;
    private boolean emailVerified;
    private Set<String> authorities;
    private BigDecimal balance;

    @Override
    public void trim() {
        firstname = firstname.trim();
        lastname = lastname.trim();
        email = email.trim();
        password = password.trim();
        authorities = authorities.stream()
                .map(String::trim)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    @Override
    public void nullToEmpty() {
        firstname = (firstname == null)? "" : firstname;
        lastname = (lastname == null)? "" : lastname;
        email = (email == null)? "" : email;
        password = (password == null)? "" : password;
        if (authorities == null) authorities = new HashSet<>(0);
        if (balance == null) balance = BigDecimal.ZERO;
    }
}
