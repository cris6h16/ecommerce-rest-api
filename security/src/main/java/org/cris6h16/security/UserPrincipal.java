package org.cris6h16.security;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UserPrincipal extends User {
    private Long id;

    public UserPrincipal(Collection<? extends GrantedAuthority> authorities, Long id, boolean enabled) {
        super("username",
                "password",
                enabled,
                true,
                true,
                true, authorities);
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                "authorities=" + getAuthorities() +
                '}';
    }
}
