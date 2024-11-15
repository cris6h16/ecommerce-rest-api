package org.cris6h16.facades;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
