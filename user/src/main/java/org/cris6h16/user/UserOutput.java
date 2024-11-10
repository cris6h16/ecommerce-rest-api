package org.cris6h16.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOutput {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Set<String> authorities;
    private boolean enabled;
    private boolean emailVerified;
}
