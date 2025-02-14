package org.cris6h16.facades;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class ResetPasswordDTO {
    private String email;
    private String code;
    private String password;
}
