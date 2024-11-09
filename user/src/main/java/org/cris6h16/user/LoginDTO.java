package org.cris6h16.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDTO {
    private String email;
    private String password;
}
