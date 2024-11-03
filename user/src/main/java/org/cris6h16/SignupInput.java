package org.cris6h16;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SignupInput implements Prepareable {
    public static final String DEF_AUTHORITY = "ROLE_USER";

    private String firstname;
    private String lastname;
    private String email;
    @Setter
    private String password;

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
    }
}
