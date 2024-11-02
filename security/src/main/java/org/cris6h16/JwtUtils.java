package org.cris6h16;

 interface JwtUtils {
    String genAccessToken(GenAccessTokenInput input);

    String genRefreshToken(Long id);
}