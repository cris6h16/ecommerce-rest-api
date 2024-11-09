package org.cris6h16.user.DTOs;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResetPasswordDTO implements Prepareable {
    private String email;
    private String code;
    private String password;

    @Override
    public void trim() {
        email = email.trim();
        code = code.trim();
        password = password.trim();
    }

    @Override
    public void nullToEmpty() {
        email = (email == null)? "" : email;
        code = (code == null)? "" : code;
        password = (password == null)? "" : password;
    }
}
