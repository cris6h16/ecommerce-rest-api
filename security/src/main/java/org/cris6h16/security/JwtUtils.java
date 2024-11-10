package org.cris6h16.security;

import java.util.Collection;

interface JwtUtils {
    String genAccessToken(GenAccessTokenInput input);

    String genRefreshToken(Long id);

     boolean validate(String token);

     Long getId(String token);

     Collection<String> getAuthority(String token);
 }