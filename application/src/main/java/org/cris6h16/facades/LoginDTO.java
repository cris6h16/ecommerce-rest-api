package org.cris6h16.facades;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDTO {
    private String email;
    private String password;
}
