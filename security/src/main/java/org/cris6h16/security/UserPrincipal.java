package org.cris6h16.security;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@ToString
public class UserPrincipal extends User {
    private Long id;

    public UserPrincipal(Collection<? extends GrantedAuthority> authorities, Long id) {
        super("username",
                "password",
                true,
                true,
                true,
                true, authorities);
        this.id = id;
    }

}
