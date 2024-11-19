package org.cris6h16.security;

import java.util.Set;

public interface SecurityComponent {
    String encodePassword(String password);
    boolean matches(String rawPassword, String encodedPassword);
    String generateRefreshToken(Long id, Set<String> authorities) ;
    String generateAccessToken(Long id, Set<String> authorities);

    Long getCurrentUserId();

    Set<String> getCurrentUserAuthorities();

    boolean isTokenValid(String token);
}
