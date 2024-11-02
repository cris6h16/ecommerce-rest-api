package org.cris6h16;

public interface SecurityService {
    String encodePassword(String password);
    boolean matches(String rawPassword, String encodedPassword);
    String generateAccessToken(GenAccessTokenInput input);
    String generateRefreshToken(Long id);
}
