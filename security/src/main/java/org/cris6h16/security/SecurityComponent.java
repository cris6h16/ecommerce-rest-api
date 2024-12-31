package org.cris6h16.security;

import java.util.Set;

public interface SecurityComponent {
    String encodePassword(String password);
    boolean matches(String rawPassword, String encodedPassword);
    String generateRefreshToken(Long id, String authority) ;
    String generateAccessToken(Long id, String authority);

    Long getCurrentUserId();

   String getCurrentUserAuthority();

    boolean isTokenValid(String token);
}
