package org.cris6h16;

// no es parte del componente usuario
public interface PasswordEncoder {
    String encode(String password);
    boolean matches(String password, String encodedPassword);
}
